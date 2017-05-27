package pl.hamerhed.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(generator = "uuid2")
	//@GenericGenerator(name="uuid2", strategy="uuid2")
	//@Column(name="id", columnDefinition="BINARY(16)")
	@Column(name="id")
	private UUID id = UUID.randomUUID();
	
	@Version
	private Long version;
	
	@Column(nullable=false, name="creation_date")
	private Date creationDate = new Date();
	
	@Transient
	private Boolean state;
	
	protected AbstractBaseEntity(){
		//id = UUID.randomUUID();
		state = Boolean.FALSE;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public UUID getId() {
		return id;
	}

	
	

/*	@Override
	public boolean isNew() {
		boolean result = false;
		if(Boolean.FALSE == state) result = true;
		System.out.println("czy jestem nowy " + result);
		return result;
	}*/


	public Long getVersion() {
		return version;
	}


	public void setVersion(Long version) {
		this.version = version;
	}


	@Override
	public int hashCode() {
		if(id != null) return id.hashCode();
		
		return super.hashCode();
	}
 
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (this == obj)
			return true;

		if (!(obj instanceof AbstractBaseEntity)) {
			return false;
		}
		AbstractBaseEntity other = (AbstractBaseEntity) obj;
		return getId().equals(other.getId());
	}


	
	public Date getCreationDate() {
		return creationDate;
	}


	private void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
