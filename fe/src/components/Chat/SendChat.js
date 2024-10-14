import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import '../../css/SendChat.css';

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
        <body className='sendchat'>
            <h2 className='chattitle'>💬</h2>
            <ul className="message-list">
                {messages.map((msg, index) => (
                    <li key={index} className={`message-item ${msg.sender === localStorage.getItem('nickname') ? 'message-right' : 'message-left'}`}>
                        <div className={msg.sender === localStorage.getItem('nickname') ? 'message-right' : 'message-left'}>
                            {msg.content}
                            {/* <span className="time">{new Date(msg.time).toLocaleString()}</span> */}
                        </div>
                    </li>
                ))}
            </ul>
            <div className='chatinputbox'>
                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder="메세지를 입력하세요."
                    disabled={!connected}
                    className="chat-input-container"

s                />
                <button onClick={sendMessage} disabled={!connected} className='chatbtn'>
                    전송
                </button></div>
        </body>
    );
};

export default SendChat;
