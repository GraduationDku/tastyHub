import React, { useState, useEffect } from 'react';

const MainChat = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const response = await fetch('http://localhost:8080/chatroom');
                const data = await response.json();
                setChatRooms(data.chatRoomDtoList);
            } catch (error) {
                console.error('Error fetching chat rooms:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchChatRooms();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="main-chat">
            <h1>Chat Rooms</h1>
            <ul>
                {chatRooms.map(chatRoom => (
                    <li key={chatRoom.chatRoomId}>
                        <h2>{chatRoom.chatTitle}</h2>
                        {chatRoom.from && (
                            <div>
                                <p><strong>From:</strong> {chatRoom.from}</p>
                                <p><strong>Message:</strong> {chatRoom.text}</p>
                                <p><strong>Time:</strong> {chatRoom.time}</p>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MainChat;
