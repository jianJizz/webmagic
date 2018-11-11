package com.hengtiansoft.learn;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class GitHubPepoPageProcessor implements PageProcessor{
	
	//  部分一：抓取网站的相关配置，包括编码抓取时间、重试次数
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	
	@Override
	// process 是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		// 部分二：定义如何抽取页面信息，并保存下来
		page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
		page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
		
		if (page.getResultItems().get("name") == null) {
			page.setSkip(true);
		}
		page.putField("readme", page.getHtml().links().regex("https://github\\.com/[\\w\\-]+[\\w\\-]+").all());
	}

	@Override
	public Site getSite() {
		return site;
	}
	
	public static void main(String[] args) {
		Spider.create(new GitHubPepoPageProcessor())
		//从"https://github.com/code4craft"开始抓
		.addUrl("https://github.com/code4craft")
		//开启5个线程抓取
		.thread(5)
		//启动爬虫
		.run();
	}
}
