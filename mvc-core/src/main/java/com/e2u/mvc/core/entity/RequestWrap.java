package com.e2u.mvc.core.entity;

import com.e2u.mvc.core.common.RequestType;

public class RequestWrap {
	private RequestType requestType;
	private String path;

	public RequestWrap() {
	}

	public RequestWrap(RequestType requestType, String path) {
		super();
		this.requestType = requestType;
		this.path = path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result
				+ ((requestType == null) ? 0 : requestType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RequestWrap))
			return false;
		RequestWrap other = (RequestWrap) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (requestType != other.requestType)
			return false;
		return true;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "RequestWrap [requestType=" + requestType + ", path=" + path
				+ "]";
	}

}
