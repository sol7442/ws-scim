package com.wowsanta.scim.repository;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Object;

public class ResourceColumn extends SCIM_Object {
	static transient Logger logger = LoggerFactory.getLogger(ResourceColumn.class);
	
	private String name;
	private String label;
	private int precision;
	private int displaySize;
	private int type;
	private String typeName;
	private String className;
	private int isNullable;
	private boolean autoIncrement;
	private boolean caseSensitive;
	private boolean currency;
	private boolean definitelyWritable;
	private boolean readOnly;
	private boolean signed;
	private boolean writable;
	private boolean searchable;
	
	private boolean primary;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getIsNullable() {
		return isNullable;
	}
	public void setIsNullable(int isNullable) {
		this.isNullable = isNullable;
	}
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	public boolean isCurrency() {
		return currency;
	}
	public void setCurrency(boolean currency) {
		this.currency = currency;
	}
	public boolean isDefinitelyWritable() {
		return definitelyWritable;
	}
	public void setDefinitelyWritable(boolean definitelyWritable) {
		this.definitelyWritable = definitelyWritable;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public boolean isSigned() {
		return signed;
	}
	public void setSigned(boolean signed) {
		this.signed = signed;
	}
	public boolean isWritable() {
		return writable;
	}
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	public boolean isSearchable() {
		return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
	private String attributeSchema;
	private String defaultValue;
	private DataMapper dataMapper;
	
	public String getAttributeSchema() {
		return attributeSchema;
	}
	public void setAttributeSchema(String attributeSchema) {
		this.attributeSchema = attributeSchema;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public DataMapper getDataMapper() {
		return dataMapper;
	}
	public void setDataMapper(DataMapper dataMapper) {
		this.dataMapper = dataMapper;
	}
	
	public Object convertMappingData(Object data) {
		Object convert_data = null;
		try {
			if(this.dataMapper == null && data == null) {
				convert_data = null;
			}else if(this.dataMapper == null && data != null) {
				convert_data = data;
			}else if(this.dataMapper != null && data == null) {
				convert_data = this.dataMapper.getDefault();
				if(convert_data == null) {
					if(dataMapper.getClassName() != null) {
						convert_data = convertByCustomMethod(data);
					}else {
						convert_data = null;
					}
				}
			}else {
				if(dataMapper.getClassName() != null) {
					convert_data = convertByCustomMethod(data);
				}else {
					convert_data = data;
				}
			}
			logger.debug("convert {} : {} > {} ", this.id, data,convert_data);
		}catch (Exception e) {
			logger.info("convert {} : {} > {} ", this.id, data, e.getMessage());
		}
		
		return convert_data;
	}
	private Object convertByCustomMethod(Object data) throws RepositoryException {
		Object convert_data = null;
		try {
			Class convert_class = Class.forName(dataMapper.getClassName());			
			Object covert_object = convert_class.newInstance();
			
			Class[] covert_params = {Object.class};
			Method method = convert_class.getMethod(dataMapper.getMethodName(), covert_params);
			
			if(data == null) {
				Object[] args = new Object[] {null};
				convert_data = method.invoke(covert_object, args );
			}else {
				convert_data = method.invoke(covert_object, data);	
			}
			
		}catch (Exception e) {
			logger.info("convert {} : {} > {} ", this.id, data, e.getMessage());
		}
		return convert_data;
	}
	public Object convertMappingData(Resource_Object resource) {
		Object data = null;
		try {
			if(this.attributeSchema != null) {
				data = resource.get(this.attributeSchema);
			}
			if(data == null) {
				data = this.defaultValue;
			}
			if(this.dataMapper != null) {
				data = convertMappingData(data);
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		logger.debug("{}={}",this.name, data);
		
		return data;
	}
	
	public String toString() {
		return toString(false);
	}
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			if(pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson  = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
}
