package crawlPic;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.*;

public class crawPicTool {
	public static int start = 1;
	public static String indexFile = "/Users/caohao/Work/simPicSearch/index/index.txt";
	public static void crawPage(String url_str)
	{
	    try {
			//out.println(url_str);
			URL url = new URL(url_str);
			String htmlContext = "";
			HttpURLConnection conn;  
			try {  
				//out.println(url_str); 
				conn = (HttpURLConnection) url.openConnection();  
				conn.setRequestProperty("User-Agent", // 很重要  
						"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) "  
						+ " Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");  
				conn.setRequestMethod("GET");  
				Scanner scanner = new Scanner(conn.getInputStream());  
				while (scanner.hasNextLine()) {  
					htmlContext+=scanner.nextLine();
					//out.println(htmlContext); 
				}  
			
			} catch (IOException e) {  
				e.printStackTrace();  
			}
			

			Pattern p2 = Pattern.compile("tag-category\" href=\"(.*?)cid=(\\d+)(.*?)\"");
			Matcher m2 = p2.matcher(htmlContext);
			String cateid = "";
			while(m2.find()&&cateid.equals(""))
			{
				cateid = m2.group(2);
				//out.println(m.group(2));
				//System.out.println("cateid:"+cateid);
				break;
			}
			
			Pattern p = Pattern.compile("pics=\"(.*?)\"");
			Matcher m = p.matcher(htmlContext);
			String pics_tmp = "";
			while(m.find())
			{
				pics_tmp+=m.group(1)+",";
				//out.println(m.group(2));
			}
			
			
			if(pics_tmp.length()>0)
			{
				String[] urls = pics_tmp.split(",");
				for(int k=0;k<urls.length;k++)
					crawlPic(url_str,urls[k],cateid);
				
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void crawlPic(String deal_url,String url_str, String cateid)
	{
		if(cateid.equals(""))
			return;
	    try {
			//out.println(url_str);
			URL url = new URL(url_str);
			String htmlContext = "";
			HttpURLConnection conn;  
			try {  
				//out.println(url_str); 
				conn = (HttpURLConnection) url.openConnection();  
				conn.setRequestProperty("User-Agent", // 很重要  
						"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) "  
						+ " Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");  
				conn.setRequestMethod("GET");  
				InputStream in = conn.getInputStream();
				FileOutputStream fo = new FileOutputStream(new File("/Users/caohao/Work/simPicSearch/images/example"+start+".jpg"));
				start++;
                byte[] buf = new byte[1024];  
                int length = 0;  
                System.out.println("开始下载:" + url);  
                while ((length = in.read(buf, 0, buf.length)) != -1) {  
                    fo.write(buf, 0, length);  
                }  
                in.close();  
                fo.close();  
                System.out.println(url_str + "下载完成");  
                FileWriter writer = new  FileWriter(indexFile,  true );  
                writer.write((start-1)+"\t"+url_str+"\t"+deal_url+"\t"+cateid+"\n");
                //System.out.println((start-1)+"\t"+url_str+"\t"+deal_url+"\t"+cateid+"\n");
                writer.close();
			
			} catch (IOException e) {  
				e.printStackTrace();  
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		
		for(int i=27169083;i<27169383;i++)
		{
			String url_str = "http://i.meituan.com/deal/"+i+".html";
			crawPage(url_str);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		String url_str = "http://i.meituan.com/deal/"+27169381+".html";
		crawPage(url_str);*/
	}
	
}
