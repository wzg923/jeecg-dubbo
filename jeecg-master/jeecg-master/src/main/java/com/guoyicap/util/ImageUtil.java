package com.guoyicap.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

/**  
* @ClassName: ImageUtil  
* @Description:图片处理类
* @author 王振广
* @date 2017年1月17日 上午10:52:32  
*    
*/
public class ImageUtil {

	/**
	 * 按指定宽高压缩图片
	 * @param srcImageFile 原图片路径
	 * @param destinationFile 压缩后路径
	 * @param height 高
	 * @param width 宽
	 * @param isPadding 是否补白
	 */
	public final static void scale2(String srcImageFile, String destinationFile, int height, int width, boolean isPadding) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (isPadding) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            if (itemp instanceof BufferedImage) {
            	ImageIO.write((BufferedImage) itemp, "JPEG", new File(destinationFile));				
			}else{				
				ImageIO.write(convertToBufferedImage(itemp), "JPEG", new File(destinationFile));
			}            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/** 
	* 传入byte[],传出byte[] 
	* @author  
	* @param imageByte 图片字节数组 
	* @param width 生成小图片宽度 
	* @param height 生成小图片高度 
	* @param gp 是否等比缩放 
	* @return 
	*/  
	public static byte[] compressPic(byte[] imageByte, int width, int height, boolean gp) {
		byte[] inByte = null;
		try {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(imageByte);
			Image img = ImageIO.read(byteInput);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				return inByte;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (gp == true) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) width + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) height + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = width; // 输出的图片宽度
					newHeight = height; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);
				img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);

				ImageWriter imgWrier;
				ImageWriteParam imgWriteParams;
				// 指定写图片的方式为 jpg
				imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
				imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
				// // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
				// imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
				// // 这里指定压缩的程度，参数qality是取值0~1范围内，
				// imgWriteParams.setCompressionQuality((float)45217/imageByte.length);
				//
				// imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
				// ColorModel colorModel = ColorModel.getRGBdefault();
				// // 指定压缩时使用的色彩模式
				// imgWriteParams.setDestinationType(new
				// javax.imageio.ImageTypeSpecifier(colorModel, colorModel
				// .createCompatibleSampleModel(100, 100)));
				// 将压缩后的图片返回字节流
				ByteArrayOutputStream out = new ByteArrayOutputStream(imageByte.length);
				imgWrier.reset();
				// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
				// OutputStream构造
				imgWrier.setOutput(ImageIO.createImageOutputStream(out));
				// 调用write方法，就可以向输入流写图片
				imgWrier.write(null, new IIOImage(tag, null, null), imgWriteParams);
				out.flush();
				out.close();
				byteInput.close();
				inByte = out.toByteArray();

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return inByte;
	}  
	
	
	public static BufferedImage convertToBufferedImage(Image image)
    {
        BufferedImage newImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
