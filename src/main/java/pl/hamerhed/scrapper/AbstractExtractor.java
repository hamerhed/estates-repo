package pl.hamerhed.scrapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import pl.hamerhed.domain.ExtractionSourceType;

public abstract class AbstractExtractor<R> implements IExtractor<R>{
	
	protected R extractionSources;
	
	@Autowired
	private DocumentFromUrlExtractor extractor;
	
	protected AbstractExtractor(){}
	
	protected Document getDocumentFromLink(String url) throws IOException {
		extractor = new DocumentFromUrlExtractor();
		return extractor.getDocumentFromLink(url);
	}
	
	protected String unescapeString(String data) {
		return StringEscapeUtils.unescapeHtml(data).trim();
	}
	
	protected Date formatDate(String dateStr, String format) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(dateStr);
	}

	protected pl.hamerhed.domain.ExtractionSource createExtractionSource(String content, String url, ExtractionSourceType type){
		pl.hamerhed.domain.ExtractionSource item = new pl.hamerhed.domain.ExtractionSource();
		item.setContent(content);
		item.setOriginalLink(url);
		item.setProcessedTimestamp(new Date());
		item.setType(type);
		return item;
	}
	
	@Override
	public R getExtractedSources() {
		return extractionSources;
	}

	public void setExtractedSources(R source) {
		this.extractionSources = source;
	}
}
