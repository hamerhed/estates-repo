package pl.hamerhed.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="extraction_source")
public class ExtractionSource extends AbstractBaseEntity implements IExtractionSource {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6717232151930532010L;

	@Column(nullable=false, name="original_link", length=1024)
	private String originalLink;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private ExtractionSourceType type;
	
	@Column(nullable=false, length=1000000)
	private String content;
	
	@Column(nullable=false, name="processed_date")
	private Date contentProcessedTimestamp;
	
	public ExtractionSource() {
		super();
	}
	
	
	@Override
	public String getOriginalLink() {
		return originalLink;
	}

	@Override
	public void setOriginalLink(String originalLink) {
		this.originalLink = originalLink;

	}

	@Override
	public ExtractionSourceType getType() {
		return type;
	}

	@Override
	public void setType(ExtractionSourceType type) {
		this.type = type;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public Date getProcessedTimestamp() {
		return contentProcessedTimestamp;
	}

	@Override
	public void setProcessedTimestamp(Date processedTimestamp) {
		this.contentProcessedTimestamp = processedTimestamp;
	}

}
