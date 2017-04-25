package com.jkx4rh.client.tool;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import android.text.TextUtils;

import com.jkx4rh.client.net.MyTask;

public class HttpUtils
{
	public static final int TIME_OUT = 10000;
	public static final String CHARSET = "UTF-8";

	private static final String BOUNDARY = getBoundry();
	private static final String MP_BOUNDARY = "--" + BOUNDARY;
	private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
	

	public static String sendFormData(String url, Parameters params, Rein rein, MyTask mTask, HttpClient client)
	{
		String result = "";
		HttpPost post=null;
		HttpResponse response=null;
		ByteArrayOutputStream bos=null;
		
		try
		{
			post = new HttpPost(url);
			
			post.setHeader("SOURCE", "1");
			if (rein != null)
			{
				rein.setHttpUriRequest(post);
			}
			if (mTask.mRequestHeader != null)
			{
				// 添加网络请求头
				Iterator<Entry<String, String>> i = mTask.mRequestHeader.entrySet().iterator();
				while (i.hasNext())
				{
					Entry<String, String> o = i.next();
					String key = o.getKey();
					String value = mTask.mRequestHeader.get(key);
					if (key == null || value == null)
					{
						continue;
					}
					post.setHeader(key, value);
				}
			}

			post.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			bos = new ByteArrayOutputStream();
			paramToUpload(bos, params);
			
			byte[] data = bos.toByteArray();
			bos.close();
			ByteArrayEntity formEntity = new ByteArrayEntity(data);
			post.setEntity(formEntity);
			response = client.execute(post);
			result = readHttpResponse(response);
		}
		catch (Exception e)
		{
			response=null;
		}
		finally
		{
			try
			{
				bos.close();
				bos=null;
				
			}
			catch (Exception msg)
			{
				bos=null;
			}
		}
		
		if (response==null)
		{
			if (post != null)
			{
				post.abort();
			}
			if (client != null)
			{
				client.getConnectionManager().shutdown();
			}
			result=null;
		}
		return result;
	}

	private static void paramToUpload(OutputStream os, Parameters params) throws Exception
	{
		if (params == null || params.size() == 0)
		{
			return;
		}
		for (int i = 0; i < params.size(); i++)
		{
			StringBuilder temp = new StringBuilder();
			temp.append(MP_BOUNDARY).append("\r\n");

			String key = params.getKey(i);
			FileInputStream fis = null;

			if (params.isFile(key))
			{
				//是一个需要上传文件的参数
				String imgpath = params.getValue(key);
				if (TextUtils.isEmpty(imgpath))
				{
					continue;
				}
				temp.append("Content-Disposition: form-data;name=\"" + key + "\";filename=\"").append(imgpath).append("\"\r\n");
				String filetype = "image/png";
				temp.append("Content-Type: ").append(filetype).append("\r\n\r\n");
				byte[] res = temp.toString().getBytes();
				
				try
				{
					os.write(res);
					fis = new FileInputStream(imgpath);
					byte[] buffer = new byte[1024 * 50];
					while (true)
					{
						int count = fis.read(buffer);
						if (count == -1)
						{
							break;
						}
						os.write(buffer, 0, count);
					}
					os.write("\r\n".getBytes());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (null != fis)
					{
						try
						{
							fis.close();
						}
						catch (IOException e)
						{
							fis=null;
						}
					}
				}
			}
			else
			{
				temp.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
				temp.append(params.getValue(key)).append("\r\n");
				byte[] res = temp.toString().getBytes();
				try
				{
					os.write(res);
				}
				catch (IOException e)
				{
					os.close();
					fis.close();
					fis=null;
					os=null;
				}
			}
		}
		os.write((END_MP_BOUNDARY + "\r\n").getBytes());
	}

	/**
	 * 读取HttpResponse数据
	 */
	private static String readHttpResponse(HttpResponse response)
	{
		String result = "";
		HttpEntity entity = response.getEntity();
		InputStream inputStream=null;
		try
		{
			inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			Header header = response.getFirstHeader("Content-Encoding");
			if (header != null && header.getValue().toLowerCase(Locale.CHINA).indexOf("gzip") > -1)
			{
				inputStream = new GZIPInputStream(inputStream);
			}
			int readBytes = 0;
			byte[] sBuffer = new byte[512];
			while ((readBytes = inputStream.read(sBuffer)) != -1)
			{
				content.write(sBuffer, 0, readBytes);
			}
			result = new String(content.toByteArray());
			return result;
		}
		catch (Exception e)
		{
			result=null;
		}
		finally
		{
			if (entity != null)
			{
				try
				{
					entity.consumeContent();
				}
				catch (IOException e)
				{
					entity = null;
				}
			}

			if (inputStream != null)
			{

				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					inputStream = null;
				}
			}
		}
		
		return result;
	}

	/**
	 * 产生11位的boundary
	 */
	static String getBoundry()
	{
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++)
		{
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0)
			{
				_sb.append((char) time % 9);
			}
			else if (time % 3 == 1)
			{
				_sb.append((char) (65 + time % 26));
			}
			else
			{
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}
}
