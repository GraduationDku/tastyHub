import React, { useState, useEffect } from 'react';

const PageButton = ({ totalItems, itemsPerPage, onPageChange }) => {
  const [currentPage, setCurrentPage] = useState(0); // 현재 페이지는 1부터 시작
  const totalPages = Math.ceil(totalItems / itemsPerPage); // 전체 페이지 수 계산

  useEffect(() => {
    onPageChange(currentPage); // 페이지가 변경될 때마다 부모 컴포넌트에 알림
  }, [currentPage, onPageChange]);

  const handleNext = () => {
    if (currentPage < totalPages) {
      setCurrentPage(prevPage => prevPage + 1);
    }
  };

  const handlePrev = () => {
    if (currentPage > 1) {
      setCurrentPage(prevPage => prevPage - 1);
    }
  };

  return (
    <div>
      <button onClick={handlePrev} disabled={currentPage === 1}>
        이전
      </button>
      <span>{currentPage} / {totalPages}</span>
      <button onClick={handleNext} disabled={currentPage === totalPages}>
        다음
      </button>
    </div>
  );
};

export default PageButton;
