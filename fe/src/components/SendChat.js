import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';

const SOCKET_URL = `ws://localhost:8080/chat`;

const SendChat = ({ roomId, username }) => {
    const [client, setClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState('');

    useEffect(() => {
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
        if (client && connected) {
            const message = {
                from: username,
                text: input,
            };
            client.publish({
                destination: `/app/rooms/${roomId}`,
                body: JSON.stringify(message),
            });
            setInput('');
        }
    };

    return (
        <div>
            <ul>
                {messages.map((msg, index) => (
                    <li key={index}>
                        <strong>{msg.from}</strong>: {msg.text}
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

export default SendChat;
