package com.agloco.mail;

import java.io.*;
import javax.activation.DataSource;

/**
 * Define InputStream's DataSource
 * @author Harry Sun
 */
class InputStreamDataSource
	implements DataSource 
{

	private InputStream is;

	public InputStreamDataSource(InputStream is)
	{
		this.is = is;
	}

	public InputStream getInputStream()
		throws IOException
	{
		is.reset();
		return is;
	}

	public OutputStream getOutputStream()
		throws IOException
	{
		return null;
	}

	public String getContentType()
	{
		return "application/octet-stream;";
	}

	public String getName()
	{
		return "attachment";
	}
}
