import React, { useState, useEffect } from "react";
import '../../css/Post.css';

function Post({ setScreen, onPostSelect, isGuest }) {
  const [posts, setPosts] = useState([]);
  const [deleteMode, setDeleteMode] = useState(false);
  const [selectedPosts, setSelectedPosts] = useState(new Set());
  const [page, setPage] = useState(0); // 현재 페이지 (0부터 시작)
  const [size, setSize] = useState(5); // 페이지 당 아이템 수 기본값
  const [sort, setSort] = useState('createdAt'); // 정렬 방식 기본값
  const [totalItems, setTotalItems] = useState(0); // 전체 게시글 수

  // 리프레시 토큰을 이용하여 엑세스 토큰 갱신하는 함수
  async function refreshAccessToken() {
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/auth/refresh`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          refreshToken: localStorage.getItem('refreshToken')
        }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('accessToken', data.accessToken);
        return data.accessToken;
      } else {
        // 리프레시 토큰이 유효하지 않은 경우 로그아웃 처리
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        setScreen('login');
      }
    } catch (error) {
      console.error('Error refreshing access token:', error);
    }
  }

  // 게시글 목록을 가져오는 함수
  async function fetchAllPost() {
    try {
      const accessToken = localStorage.getItem('accessToken');
      const response = await fetch(
        `${process.env.REACT_APP_API_URL}/post/list?page=${page}&size=${size}&sort=${sort}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': accessToken
          }
        }
      );

      if (response.status === 401) { // 엑세스 토큰이 만료된 경우
        const newAccessToken = await refreshAccessToken();
        if (newAccessToken) {
          return fetchAllPost(); // 새 토큰으로 다시 호출
        }
      } else if (response.ok) {
        const data = await response.json();
        setPosts(data.content); // 받아온 게시글 설정
        setTotalItems(data.totalItems); // 전체 게시글 수 설정
      }
    } catch (error) {
      console.error('Error fetching posts:', error);
    }
  }

  useEffect(() => {
    if (isGuest) {
      setScreen('signup');
      return;
    }
    fetchAllPost(); // 페이지나 기타 변수가 변경될 때마다 호출
  }, [page, size, sort, setScreen, isGuest]);

  const handleDeleteModeToggle = () => {
    setDeleteMode(!deleteMode);
    setSelectedPosts(new Set());
  };

  const handleCheckboxChange = (postId) => {
    const updatedSelectedPosts = new Set(selectedPosts);
    if (updatedSelectedPosts.has(postId)) {
      updatedSelectedPosts.delete(postId);
    } else {
      updatedSelectedPosts.add(postId);
    }
    setSelectedPosts(updatedSelectedPosts);
  };

  const handleDeleteSelected = async () => {
    for (let postId of selectedPosts) {
      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL}/post/delete/${postId}`,
          {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': localStorage.getItem('accessToken')
            }
          }
        );
        if (!response.ok) {
          throw new Error(`Failed to delete post with id ${postId}`);
        }
      } catch (error) {
        console.error('Error deleting post:', error);
      }
    }
    setPosts(posts.filter(post => !selectedPosts.has(post.postId)));
    setSelectedPosts(new Set());
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
    setPage(newPage); // 새로운 페이지 설정
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
      <h1>재료 공유 게시글 조회</h1>
      <button onClick={() => setScreen('createpost')}>게시글 작성하기</button>
      <button onClick={handleDeleteModeToggle}>
        {deleteMode ? '취소' : '삭제하기'}
      </button>
      {deleteMode && (
        <button onClick={handleDeleteSelected} disabled={selectedPosts.size === 0}>
          선택된 게시글 삭제
        </button>
      )}
      <br/><br/>
      <div className="postbox">
        <div className='select-container'>
          <select value={sort} onChange={handleSortChange}>
            <option value="createdAt">날짜</option>
            <option value="title">제목</option>
          </select>

          <select value={size} onChange={handleSizeChange}>
            <option value={5}>5개</option>
            <option value={10}>10개</option>
            <option value={20}>20개</option>
          </select>
        </div>
        <ul>
          {posts.map(post => (
            <li key={post.postId}>
              <div className="seperate">
                {deleteMode && (
                  <input
                    type="checkbox"
                    checked={selectedPosts.has(post.postId)}
                    onChange={() => handleCheckboxChange(post.postId)}
                  />
                )}
                <h3 onClick={() => !deleteMode && onPostSelect(post.postId)}>
                  {post.title}
                </h3>
                <div className="seperate">
                  <p>{post.nickname || '정보 없음'}</p>
                  <p>{post.postState || '정보 없음'}</p>
                </div>
              </div>
            </li>
          ))}
        </ul>
        <PageButton
          totalItems={totalItems}
          itemsPerPage={size}
          currentPage={page}
          onPageChange={handlePageChange}
        />
      </div>
    </body>
  );
}

export default Post;
