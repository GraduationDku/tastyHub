import React, { useState, useEffect } from 'react';

function MainChat({ onChatroomSelect, setScreen }) {
    const [chatRooms, setChatRooms] = useState([]);

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const response = await fetch('http://localhost:8080/room', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.getItem('accessToken')
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    if (Array.isArray(data)) {
                        // 중복된 채팅방 제거
                        const uniqueChatRooms = Array.from(new Set(data.map(room => room.roomId)))
                            .map(roomId => {
                                return data.find(room => room.roomId === roomId);
                            });
                        setChatRooms(uniqueChatRooms);
                    } else {
                        console.error('Invalid data format:', data);
                    }
                }
            } catch (error) {
                console.error('Error fetching chat rooms:', error);
            }
        };

        fetchChatRooms();
    }, []);

    const handleChatroomClick = (roomId) => {
        onChatroomSelect(roomId);
        setScreen('sendchat');
    };

    return (
        <div className="main-chat">
            <h1>Chat Rooms</h1>
            <ul>
                {chatRooms.map(chatRoom => (
                    <li key={chatRoom.roomId}>
                        <div>
                            <p>{chatRoom.from}</p>
                            <p>{chatRoom.time}</p>
                            <button onClick={() => handleChatroomClick(chatRoom.roomId)}>
                                {chatRoom.chatRoomTitle}
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default MainChat;
