package com.wowsanta.daemon;

public interface DaemonService {
	public void init(String config_file) throws DaemonException;
	public void start() throws DaemonException;
	public void stop() throws DaemonException;
	public void destory();
}
