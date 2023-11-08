package com.ruoyi.common.utils;

import com.ruoyi.common.exception.ServiceException;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtils {

	/**
	 * 使用 gzip 进行压缩.
	 *
	 * @param str 压缩前的文本
	 * @return 压缩后的文本（BASE64 编码）
	 * @throws IOException 如果解压异常
	 */
	public static String gzip(final String str) {
		if (str == null || "".equals(str)) {
			return str;
		}
		String ret = null;
		try {
			byte[] compressed;
			ByteArrayOutputStream out = null;
			GZIPOutputStream zout = null;
			try {
				out = new ByteArrayOutputStream();
				zout = new GZIPOutputStream(out);
				zout.write(str.getBytes());
				zout.close();
				compressed = out.toByteArray();
				ret = new String(Base64.encodeBase64(compressed), "UTF-8");
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return ret;
	}

	/**
	 * 使用 gzip 进行解压缩.
	 *
	 * @param compressedStr 压缩后的文本（BASE64 编码）
	 * @return 解压后的文本
	 * @throws IOException 如果解压异常
	 */
	public static String ungzip(final String compressedStr) throws IOException {
		if (null == compressedStr || "".equals(compressedStr)) {
			return compressedStr;
		}

		String ret = null;

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		GZIPInputStream zin = null;
		try {
			final byte[] compressed = Base64.decodeBase64(compressedStr.getBytes());
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new GZIPInputStream(in);
			final byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}

			ret = out.toString("UTF-8");
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

}
