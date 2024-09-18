import React, { useState, useEffect } from 'react';
import '../../css/Chat/MainChat.css';
import PageButton from '../../../src/components/PageButton.js';

function MainChat({ onChatroomSelect, setScreen, isGuest }) {
    const [chatRooms, setChatRooms] = useState([]);
    const [selectedRooms, setSelectedRooms] = useState(new Set());
    const [deleteMode, setDeleteMode] = useState(false);
    const [page, setPage] = useState(1); // 현재 페이지
    const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
    const [sort, setSort] = useState('date'); // 정렬 방식 기본값
    const [totalItems, setTotalItems] = useState(0); // 전체 게시글 수
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수

    useEffect(() => {
        if (isGuest) {
            setScreen('signup');
            return;
        }
        
        const fetchChatRooms = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_API_URL}/room??page=${page}&size=${size}&sort=${sort}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.getItem('accessToken')
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    setTotalItems(data.content.length); // 전체 게시글 수 계산 (posts 배열의 길이)
                    setTotalPages(Math.ceil(data.content.length / size)); // 전체 페이지 수 계산
                    
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
    }, [isGuest, setScreen, page, size, sort]);

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
                const response = await fetch(`${process.env.REACT_APP_API_URL}/room/${roomId}`, {
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

    const handleSizeChange = (e) => {
        setSize(parseInt(e.target.value, 10) || 5);
        setPage(1); // 페이지를 1로 초기화
      };
    
      const handleSortChange = (e) => {
        setSort(e.target.value || 'date');
        setPage(1); // 페이지를 1로 초기화
      };
    
      const handlePageChange = (newPage) => {
        if (newPage < 1) newPage = 1; // 페이지 번호를 1보다 작지 않도록 설정
        if (newPage > totalPages) newPage = totalPages; // 페이지 번호를 전체 페이지 수보다 크지 않도록 설정
        setPage(newPage);
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

<div>
            <label>정렬 기준: </label>
            <select value={sort} onChange={handleSortChange}>
              <option value="date">날짜</option>
              <option value="title">제목</option>
              <option value="nickname">작성자</option>
            </select>
        
            <label>게시글 수: </label>
            <select value={size} onChange={handleSizeChange}>
              <option value={5}>5개</option>
              <option value={10}>10개</option>
              <option value={20}>20개</option>
            </select>
          </div>

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

                <PageButton
                totalItems={totalItems} // 전체 게시글 수
                itemsPerPage={size} // 페이지당 게시글 수
                onPageChange={handlePageChange} // 페이지 변경 시 호출될 함수
                 />
            </div>
        </div>
    );
}

export default MainChat;
