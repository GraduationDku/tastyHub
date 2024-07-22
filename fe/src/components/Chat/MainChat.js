import React, { useState, useEffect } from 'react';
import '../css/MainChat.css';

function MainChat({ onChatroomSelect, setScreen, isGuest }) {
    const [chatRooms, setChatRooms] = useState([]);
    const [selectedRooms, setSelectedRooms] = useState(new Set());
    const [deleteMode, setDeleteMode] = useState(false);

    useEffect(() => {
        if (isGuest) {
            setScreen('signup');
            return;
        }
        
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
    }, [isGuest, setScreen]);

    const handleChatroomClick = (roomId) => {
        if (!deleteMode) {
            onChatroomSelect(roomId);
            setScreen('sendchat', { roomId });
        }
    };

    const handleDeleteModeToggle = () => {
        setDeleteMode(!deleteMode);
        setSelectedRooms(new Set());
    };

    const handleCheckboxChange = (roomId) => {
        const updatedSelectedRooms = new Set(selectedRooms);
        if (updatedSelectedRooms.has(roomId)) {
            updatedSelectedRooms.delete(roomId);
        } else {
            updatedSelectedRooms.add(roomId);
        }
        setSelectedRooms(updatedSelectedRooms);
    };

    const handleDeleteSelected = async () => {
        for (let roomId of selectedRooms) {
            try {
                const response = await fetch(`http://localhost:8080/room/${roomId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.getItem('accessToken')
                    }
                });
                if (!response.ok) {
                    throw new Error(`Failed to delete room with id ${roomId}`);
                }
            } catch (error) {
                console.error('Error deleting chat room:', error);
            }
        }
        setChatRooms(chatRooms.filter(room => !selectedRooms.has(room.roomId)));
        setSelectedRooms(new Set());
        setDeleteMode(false);
    };

    return (
        <div className="mainchat">
            <h1>채팅방</h1>
            
            <div className='box'>
            <button onClick={handleDeleteModeToggle}>
                {deleteMode ? '취소' : '삭제하기'}
            </button>
            {deleteMode && (
                <button onClick={handleDeleteSelected} disabled={selectedRooms.size === 0}>
                    선택된 채팅방 삭제
                </button>
            )}
                <ul>
                    {chatRooms.map(chatRoom => (
                        <li key={chatRoom.roomId}>
                            <div className='seperate'>
                                {deleteMode && (
                                    <input
                                        type="checkbox"
                                        checked={selectedRooms.has(chatRoom.roomId)}
                                        onChange={() => handleCheckboxChange(chatRoom.roomId)}
                                    />
                                )}
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
        </div>
    );
}

export default MainChat;
