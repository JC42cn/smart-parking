package com.smart.smartparking.common;

import cn.hutool.core.date.DateUtil;
import java.io.File;
public class CommonUtils {
	/**
	 * 创建多级文件夹
	 * @return
	 */
	public static File createParentFile(String filePath){
		File parentFile = new File(filePath+ SystemConstant.SF_FILE_SEPARATOR+ DateUtil.thisYear());
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		parentFile = new File(parentFile,(DateUtil.thisMonth()+1)+"");
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		parentFile = new File(parentFile,DateUtil.thisDayOfMonth()+"");
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return parentFile;
	}
}
