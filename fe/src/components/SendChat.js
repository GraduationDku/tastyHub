import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const SOCKET_URL = `http://localhost:8080/app/chat/rooms/${roomId}/send`;

const SendChat = ({ roomId, username }) => {
    const [client, setClient] = useState(null); //STOMP 클라이언트를 저장하는 상태 변수
    const [connected, setConnected] = useState(false); //서버와의 연결 상태 저장
    const [messages, setMessages] = useState([]); //수신한 메세지 저장
    const [input, setInput] = useState(''); //사용자가 입력한 메세지 저장하는 상태 변수

    useEffect(() => {
        const stompClient = new Client({ //STOMP 클라이언트가 서버랑 연결되었을 때 호출되는 콜백 함수
            webSocketFactory: () => new SockJS(SOCKET_URL), //웹 소켓 연결을 생성하는 팩토리 함수
            reconnectDelay: 5000, //웹 소켓 연결이 끊어졌을 때 다시 연결을 시도하는 시간 간격
            heartbeatIncoming: 4000, //클라이언트 <- 서버 하트비트 수신
            heartbeatOutgoing: 4000, // 클라이언트 -> 서버 하트비트 발신 , 하트비트 : 클라이언트와 서버 간 연결이 유효한지 주기적으로 확인해줌
            debug: (str) => {
                console.log(str); //디버그 로그 출력
            },
        });

        stompClient.onConnect = () => {
            setConnected(true);
            const authorization = response.headers.get('Authorization');
            const refreshToken = response.headers.get('Refresh');
            localStorage.setItem('accessToken', authorization);
            localStorage.setItem('refreshToken', refreshToken);
            stompClient.subscribe(`/app/chat/rooms/${roomId}`, (message) => { //클라이언트가 저 경로를 구독하여 서버로부터 해당 경로로 전송되는 메세지를 수신하는 함수
            const receivedMessage = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });
        };

        stompClient.onDisconnect = () => { //STOMP 클라이언트가 서버와의 연결이 끊어졌을 때 호출되는 콜백 함수
            setConnected(false);
        };

        stompClient.activate(); //STOMP 클라이언트 활성화 함수
        setClient(stompClient); //생성된 STOMP 클라이언트를 상태 변수에 저장

        return () => {
            stompClient.deactivate(); //컴포넌트가 언마운트될 때 클라이언트 비활성화 시킴
        };
    }, [roomId]);

    
    const sendMessage = () => {
        if (client && connected) {
            const message = {
                from: username, 
                text: input,
                time: new Date().toISOString(),
            };
            client.publish({
                destination: `/app/chat/rooms/${roomId}/send`,
                body: JSON.stringify(message),
            });
            setInput(''); //전송 후 입력 필드 비우기
        }
    };

    return (
        <div>
            <ul>
                {messages.map((msg, index) => ( //msg: 배열의 각 요소, index: 배열의 인덱스
                    <li key={index}>
                        <strong>{msg.from}</strong>: {msg.text} <em>{msg.time}</em>
                    </li>
                ))}
            </ul>
            <input
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="메세지를 입력하세요."
                disabled={!connected}
            />
            <button onClick={sendMessage} disabled={!connected}>
                전송
            </button>
        </div>
    );
};

export default Chat;
