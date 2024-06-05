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
                console.log(response);
                if (response.ok) {
                    const data = await response.json();
                    console.log(data);
                    if (Array.isArray(data)) {
                        setChatRooms(data);
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
