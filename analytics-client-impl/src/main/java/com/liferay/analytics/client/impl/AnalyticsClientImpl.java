/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.analytics.client.impl;

import com.liferay.analytics.client.AnalyticsClient;
import com.liferay.analytics.data.binding.JSONObjectMapper;
import com.liferay.analytics.data.binding.internal.AnalyticsEventsMessageJSONObjectMapper;
import com.liferay.analytics.model.AnalyticsEventsMessage;
import com.liferay.petra.json.web.service.client.JSONWebServiceClient;
import com.liferay.petra.json.web.service.client.internal.JSONWebServiceClientImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eduardo Garcia
 */
public class AnalyticsClientImpl implements AnalyticsClient {

	public String sendAnalytics(AnalyticsEventsMessage analyticsEventsMessage)
		throws Exception {

		String jsonAnalyticsEventsMessage = _jsonObjectMapper.map(
			analyticsEventsMessage);

		if (_logger.isDebugEnabled()) {
			_logger.debug(
				String.format(
					"Sending analytics message %s to destination %s//%s:%s/%s",
					jsonAnalyticsEventsMessage, _ANALYTICS_GATEWAY_PROTOCOL,
					_ANALYTICS_GATEWAY_HOST, _ANALYTICS_GATEWAY_PORT,
					_ANALYTICS_GATEWAY_PATH));
		}

		_jsonWebServiceClient.setHostName(_ANALYTICS_GATEWAY_HOST);
		_jsonWebServiceClient.setHostPort(
			Integer.parseInt(_ANALYTICS_GATEWAY_PORT));
		_jsonWebServiceClient.setProtocol(_ANALYTICS_GATEWAY_PROTOCOL);

		return _jsonWebServiceClient.doPostAsJSON(
			_ANALYTICS_GATEWAY_PATH, jsonAnalyticsEventsMessage);
	}

	private static final String _ANALYTICS_GATEWAY_HOST = System.getProperty(
		"analytics.gateway.host", "ec-dev.liferay.com");

	private static final String _ANALYTICS_GATEWAY_PATH = System.getProperty(
		"analytics.gateway.path",
		"/api/analyticsgateway/send-analytics-events");

	private static final String _ANALYTICS_GATEWAY_PORT = System.getProperty(
		"analytics.gateway.port", "8095");

	private static final String _ANALYTICS_GATEWAY_PROTOCOL =
		System.getProperty("analytics.gateway.protocol", "https");

	private static final Logger _logger = LoggerFactory.getLogger(
		AnalyticsClientImpl.class);

	private final JSONObjectMapper<AnalyticsEventsMessage> _jsonObjectMapper =
		new AnalyticsEventsMessageJSONObjectMapper();
	private final JSONWebServiceClient _jsonWebServiceClient =
		new JSONWebServiceClientImpl();

}