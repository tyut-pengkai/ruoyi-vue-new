package com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode;

import java.io.IOException;

public interface IAXMLSerialize {
	public int getSize();

	public void setSize(int size);
	
	public int getType();

	public void setType(int type);
	
	public void read(IntReader reader) throws IOException;
	public void write(IntWriter writer) throws IOException;
}
