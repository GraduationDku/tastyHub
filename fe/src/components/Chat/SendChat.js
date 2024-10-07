import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';

const SOCKET_URL = `wss://localhost/ws/chat`;

const SendChat = ({ roomId }) => {
    const [client, setClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');
    
    useEffect(() => {
        const nickname = localStorage.getItem('nickname');

        const fetchPreviousMessages = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_API_URL}/${roomId}`, {
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
                setMessages(data);
            } catch (error) {
                console.error('Failed to fetch previous messages:', error);
                setMessages([]);
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
                sender: nickname,
                content: input,
                time: new Date()
            };
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
        <div>
            <ul style={{ listStyleType: 'none', padding: 0, flex: 1, overflowY: 'auto' }}>
                {messages.map((msg, index) => (
                    <li
                        key={index}
                        style={{
                            display: 'flex',
                            justifyContent: msg.sender === localStorage.getItem('nickname') ? 'flex-end' : 'flex-start',
                            padding: '5px 10px'
                        }}
                    >
                        {msg.sender !== localStorage.getItem('nickname') && (
                            <>
                                <div>
                                    <strong>{msg.sender} </strong>
                                    {msg.content}
                                </div>
                                <div>
                                    {new Date(msg.time).toLocaleString()}
                                </div>
                            </>
                        )}
                        {msg.sender === localStorage.getItem('nickname') && (
                            <>
                                <div>
                                    {new Date(msg.time).toLocaleString()}
                                </div>
                                <div>
                                    {msg.content}
                                </div>
                            </>
                        )}
                    </li>
                ))}
            </ul>
            <div style={{ display: 'flex', position: 'fixed', bottom: 0, width: '100%', backgroundColor: '#fff', padding: '10px', boxSizing: 'border-box', borderTop: '1px solid #ccc', height:'70px'}}>
                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder="메세지를 입력하세요."
                    disabled={!connected}/>
                <button
                    onClick={sendMessage}
                    disabled={!connected}>
                    전송
                </button>
            </div>
        </div>
    );
};

export default SendChat;
