package com.wession.scim.intf;

public interface schemas_name {
	public final static String _resource_user = "User";
	public final static String _resource_group = "Group";
	
	public final static String _user_schemas = "schemas";
	public final static String _user_meta = "meta";
	public final static String _group_schemas = "schemas";
	public final static String _group_meta = "meta";
	
	public final static String _meta_resource_type = "resourceType";
	public final static String _meta_created = "created";
	public final static String _meta_last_modified = "lastModified";
	public final static String _meta_version = "version";
	public final static String _meta_location = "location";
	
	public final static String _user_external_id = "externalId";
	public final static String _user_username = "userName";
	public final static String _user_name = "name";
	public final static String _user_name_display = "displayName";
	public final static String _user_name_nick = "nickName";
	public final static String _user_profile_url = "profileUrl";
	public final static String _user_addresses = "addresses";
	public final static String _user_type = "userType";
	public final static String _user_title = "title";
	public final static String _user_preferred_language = "preferredLanguage";
	public final static String _user_locale = "locale";
	public final static String _user_timezone = "timezone";
	public final static String _user_active = "active";
	public final static String _user_password = "password";
	public final static String _user_x509 = "x509Certificates";
	
	public final static String _user_emails = "emails";
	public final static String _user_email_type = "type";
	public final static String _user_email_value = "value";
	public final static String _user_email_primary = "primary";
	
	public final static String _user_phonenumbers = "phoneNumbers";
	public final static String _user_phonenumber_type = "type";
	public final static String _user_phonenumber_value = "value";
	public final static String _user_phonenumber_primary = "primary";
	
	public final static String _user_ims = "ims";
	public final static String _user_ims_type = "type";
	public final static String _user_ims_value = "value";
	public final static String _user_ims_primary = "primary";
	
	public final static String _user_photos = "photos";
	public final static String _user_photo_type = "type";
	public final static String _user_photo_value = "value";
	public final static String _user_photo_primary = "primary";
	
	public final static String _user_groups = "groups";
	public final static String _user_groups_ref = "$ref";
	public final static String _user_groups_id = "value";
	public final static String _user_groups_display = "display";
	public final static String _user_groups_type = "type";
	
	public final static String _group_id = "id";
	public final static String _group_name_display = "displayName";
	public final static String _group_members = "members";
	public final static String _group_member_id = "value";
	public final static String _group_member_ref = "$ref";
	public final static String _group_member_type = "type";
	
}
