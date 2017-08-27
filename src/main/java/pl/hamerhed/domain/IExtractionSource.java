package pl.hamerhed.domain;

import java.util.Date;
import java.util.UUID;

public interface IExtractionSource extends IPersistable<UUID> {
	String getOriginalLink();
	void setOriginalLink(String originalLink);
	
	ExtractionSourceType getType();
	void setType(ExtractionSourceType type);
	
	String getContent();
	void setContent(String content);
	
	Date getProcessedTimestamp();
	void setProcessedTimestamp(Date processedTimestamp);
	
}
