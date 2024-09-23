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
        <div style={{ height: '100vh', display: 'flex', flexDirection: 'column', backgroundColor: '#FAF6C0' }}>
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
                                <div
                                    style={{
                                        background: '#FFF',
                                        padding: '10px',
                                        borderRadius: '10px',
                                        maxWidth: '60%',
                                        wordWrap: 'break-word',
                                        boxShadow: '0 1px 3px rgba(0,0,0,0.2)',
                                        marginLeft: '10px'
                                    }}
                                >
                                    <strong>{msg.sender} </strong>
                                    {msg.content}
                                </div>
                                <div style={{ fontSize: '0.8em', color: '#888', marginTop: '5px', marginLeft: '10px' }}>
                                    {new Date(msg.time).toLocaleString()}
                                </div>
                            </>
                        )}
                        {msg.sender === localStorage.getItem('nickname') && (
                            <>
                                <div style={{ fontSize: '0.8em', color: '#888', marginTop: '5px', marginRight: '10px' }}>
                                    {new Date(msg.time).toLocaleString()}
                                </div>
                                <div
                                    style={{
                                        background: 'white',
                                        padding: '10px',
                                        borderRadius: '10px',
                                        maxWidth: '60%',
                                        wordWrap: 'break-word',
                                        boxShadow: '0 1px 3px rgba(0,0,0,0.2)',
                                        marginRight: '10px',
                                        color : '#5C5F5C'
                                    }}
                                >
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
                    disabled={!connected}
                    style={{ flex: 1, padding: '10px', boxSizing: 'border-box', marginRight: '10px', borderRadius:'10px' , border:'1px solid #ccc'}}
                />
                <button
                    onClick={sendMessage}
                    disabled={!connected}
                    style={{ padding: '15px', boxSizing: 'border-box', border:'none', backgroundColor : '#AEC7A1', borderRadius:'10px'}}
                >
                    전송
                </button>
            </div>
        </div>
    );
};

export default SendChat;
