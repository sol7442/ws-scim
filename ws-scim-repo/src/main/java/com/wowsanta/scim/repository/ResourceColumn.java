package com.wowsanta.scim.repository;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wowsanta.scim.object.Resource_Object;
import com.wowsanta.scim.object.SCIM_Object;

public class ResourceColumn extends SCIM_Object {
	
	static transient Logger logger = LoggerFactory.getLogger(ResourceColumn.class);
	
	private String name;
	private AttributeSchema attributeSchema;
	private Map<String,Object> attributes = new HashMap<String, Object>();
	private ResourceType type = ResourceType.ValueColumn;
	private Object defaultValue;
	private DataMapper dataMapper;
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}
	public void addAttribute(String key,Object value) {
		this.attributes.put(key,value);
	}

	public DataMapper getDataMapper() {
		return dataMapper;
	}
	public void setDataMapper(DataMapper dataMapper) {
		this.dataMapper = dataMapper;
	}
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}
	public void setAttributeSchema(AttributeSchema attribute) {
		this.attributeSchema = attribute;
		logger.debug("{}-{}", name, attribute.getName());
	}
	public AttributeSchema getAttributeSchema() {
		return this.attributeSchema;
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
			
		}catch (Exception e) {
			logger.error("{} : {}",this.id,e.getMessage(),e);
		}finally {
			logger.debug("convert {} : {} > {} ", this.id, data,convert_data);
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
			logger.error("{} - {} : {}",this.id, this.dataMapper, data, e);
			throw new RepositoryException(e.getMessage(), e);
		}
		return convert_data;
	}
	public Object convertMappingData(Resource_Object resource) {
		Object data = null;
		try {
			if(this.attributeSchema != null) {
				data = resource.get(this.attributeSchema.getName());
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
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		logger.debug("defaultvalue : {}-{}",name,defaultValue);
	}


	
}
