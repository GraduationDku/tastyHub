import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';

const SOCKET_URL = `ws://localhost:8080/chat`;

const SendChat = ({ roomId }) => {
    const [client, setClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    
    useEffect(() => {
        const nickname = localStorage.getItem('nickname');

        const fetchPreviousMessages = async () => {
            try {
                const response = await fetch(`http://localhost:8080/room/${roomId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': localStorage.getItem('accessToken'),
                        'Content-Type': 'application/json'
                    }
                });
                
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
        
                const data = await response.json();
                console.log('Fetched messages:', data);
                setMessages(data);
            } catch (error) {
                console.error('Failed to fetch previous messages:', error);
                setMessages([]);  // 오류 발생 시 빈 배열로 설정
            }
        };

        fetchPreviousMessages();

        const stompClient = new Client({
            brokerURL: SOCKET_URL,
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            debug: (str) => {
                console.log(str);
            },
        });

        stompClient.onConnect = () => {
            setConnected(true);
            stompClient.subscribe(`/topic/rooms/${roomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });
        };

        stompClient.onDisconnect = () => {
            setConnected(false);
        };

        stompClient.activate();
        setClient(stompClient);

        return () => {
            stompClient.deactivate();
        };
    }, [roomId]);

    const sendMessage = () => {
        const nickname = localStorage.getItem('nickname');
        if (client && connected && nickname) {
            const message = {
                from: nickname,
                text: input,
                time: new Date().toISOString()
            };
            console.log(message.time);
            client.publish({
                destination: `/app/rooms/${roomId}`,
                body: JSON.stringify(message),
            });
            setInput('');
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            sendMessage();
        }
    };

    return (
        <div style={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
            <ul style={{ listStyleType: 'none', padding: 0, flex: 1, overflowY: 'auto' }}>
                {messages.map((msg, index) => (
                    <li
                        key={index}
                        style={{
                            display: 'flex',
                            justifyContent: msg.from === localStorage.getItem('nickname') ? 'flex-end' : 'flex-start',
                            padding: '5px 0'
                        }}
                    >
                        <div style={{ fontSize: '0.8em', color: '#888', marginTop: '5px' }}>
                                {new Date(msg.time).toLocaleString()}
                        </div>
                        <div
                            style={{
                                background: msg.from === localStorage.getItem('nickname') ? '#DCF8C6' : '#FFF',
                                padding: '10px',
                                borderRadius: '10px',
                                maxWidth: '60%',
                                wordWrap: 'break-word',
                                boxShadow: '0 1px 3px rgba(0,0,0,0.2)',
                            }}
                        >
                            {msg.from !== localStorage.getItem('nickname') && <strong>{msg.from} </strong>}
                            {msg.time}
                            {msg.text}
                        </div>
                    </li>
                ))}
            </ul>
            <div style={{ display: 'flex', position: 'fixed', bottom: 0, width: '100%', backgroundColor: '#fff', padding: '10px', boxSizing: 'border-box', borderTop: '1px solid #ccc' }}>
                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder="메세지를 입력하세요."
                    disabled={!connected}
                    style={{ flex: 1, padding: '10px', boxSizing: 'border-box', marginRight: '10px' }}
                />
                <button
                    onClick={sendMessage}
                    disabled={!connected}
                    style={{ padding: '10px', boxSizing: 'border-box' }}
                >
                    전송
                </button>
            </div>
        </div>
    );
};

export default SendChat;
