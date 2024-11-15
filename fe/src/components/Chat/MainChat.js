import React, { useState, useEffect } from 'react';
import '../../css/MainChat.css';

function MainChat({ onChatroomSelect, setScreen, isGuest }) {
    const [chatRooms, setChatRooms] = useState([]);
    const [selectedRooms, setSelectedRooms] = useState(new Set());
    const [deleteMode, setDeleteMode] = useState(false);
    const [page, setPage] = useState(0); // 현재 페이지
    const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
    const [sort, setSort] = useState('createdAt'); // 정렬 방식 기본값
    const [totalItems, setTotalItems] = useState(0); // 전체 채팅방 수

    useEffect(() => {
        if (isGuest) {
            setScreen('signup');
            return;
        }

        const fetchChatRooms = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_API_URL}/room?page=${page}&size=${size}&sort=${sort}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': localStorage.getItem('accessToken')
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    setTotalItems(data.totalItems); // 전체 채팅방 수 설정
                    setChatRooms(data.content);
                    console.log(data);
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
        setPage(0); // 페이지를 0으로 초기화
    };

    const handleSortChange = (e) => {
        setSort(e.target.value || 'createdAt');
        setPage(0); // 페이지를 0으로 초기화
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    const PageButton = ({ totalItems, itemsPerPage, currentPage, onPageChange }) => {
        const totalPages = Math.ceil(totalItems / itemsPerPage); // 전체 페이지 수 계산
        const pages = Array.from({ length: totalPages }, (_, i) => i); // 페이지 목록 생성

        return (
            <div>
                <button onClick={() => onPageChange(currentPage - 1)} disabled={currentPage === 0}>
                    이전
                </button>
                {pages.map((pageNum) => (
                    <button
                        key={pageNum}
                        onClick={() => onPageChange(pageNum)}
                        disabled={currentPage === pageNum}
                    >
                        {pageNum + 1} {/* 1부터 시작하도록 페이지 표시 */}
                    </button>
                ))}
                <button onClick={() => onPageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
                    다음
                </button>
            </div>
        );
    };

    return (
        <body>
            <h1>채팅방</h1>
            <button onClick={handleDeleteModeToggle}>
                {deleteMode ? '취소' : '삭제하기'}
            </button>
            <br />
            <br />
            <div className='chatbox'>
                {deleteMode && (
                    <button onClick={handleDeleteSelected} disabled={selectedRooms.size === 0}>
                        선택된 채팅방 삭제
                    </button>
                )}

                <div className='select-container'>
                    <select value={sort} onChange={handleSortChange}>
                        <option value="createdAt">날짜</option>
                        <option value="chatRoomTitle">제목</option>
                    </select>
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
                                <h3 onClick={() => handleChatroomClick(chatRoom.roomId)}>
                                    {chatRoom.chatRoomTitle}
                                </h3>
                                
                            </div>
                        </li>
                    ))}
                </ul>

                <PageButton
                    totalItems={totalItems} // 전체 채팅방 수
                    itemsPerPage={size} // 페이지당 채팅방 수
                    currentPage={page} // 현재 페이지
                    onPageChange={handlePageChange} // 페이지 변경 시 호출될 함수
                />
            </div>
        </body>
    );
}

export default MainChat;
