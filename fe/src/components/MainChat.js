import React, { useState, useEffect } from 'react';

function MainChat({onChatroomSelect, setScreen}){
    const [chatRooms, setChatRooms] = useState([]);

    useEffect(() => {
        const fetchChatRooms = async () => {
            try {
                const response = await fetch('http://localhost:8080/room', {
                    method : 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization' : localStorage.getItem('accessToken')
                    }
                });
                console.log(response);
                if(response.ok){
                    const data = await response.json();
                    console.log(data);
                    if(Array.isArray(data)){
                    setChatRooms(data);
                    } else {
                        console.error('Invaild data format:', data)
                    }
                }
            } catch (error) {
                console.error('Error fetching chat rooms:', error);
            }
        }

        fetchChatRooms();
    }, []);

    return (
        <div className="main-chat">
            <h1>Chat Rooms</h1>
            <ul>
                {chatRooms.map(chatRoom => (
                    <li key={chatRoom.chatRoomId}>
                        <button onClick={()=> setScreen('sendchat')}>{chatRoom.chatRoomTitle}</button>
                            <div>
                                <p><strong></strong> {chatRoom.from}</p>
                                <p><strong></strong> {chatRoom.text}</p>
                                <p><strong></strong> {chatRoom.time}</p>
                            </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MainChat;
