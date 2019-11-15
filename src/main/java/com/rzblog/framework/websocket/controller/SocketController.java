package com.rzblog.framework.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.rzblog.framework.websocket.service.WebSocketPushHandler;

@Controller
public class SocketController {
	@Autowired
	public WebSocketPushHandler webSocketPushHandler;
	
	//用于向客户端发送消息
}
