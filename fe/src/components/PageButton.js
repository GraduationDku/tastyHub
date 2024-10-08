import React, { useState, useEffect } from 'react';

const PageButton = ({ totalItems, itemsPerPage, onPageChange }) => {
  const totalPages = Math.ceil(totalItems / itemsPerPage); // 전체 페이지 수 계산

  const [currentPage, setCurrentPage] = useState(0); // 현재 페이지는 0부터 시작

  useEffect(() => {
    onPageChange(currentPage); // 페이지가 변경될 때마다 부모 컴포넌트에 알림
  }, [currentPage, onPageChange]);

  const handleNext = () => {
    if (currentPage < totalPages - 1) { // 마지막 페이지는 totalPages - 1
      setCurrentPage(prevPage => prevPage + 1);
    }
  };

  const handlePrev = () => {
    if (currentPage > 0) { // 현재 페이지가 0보다 클 때만 감소
      setCurrentPage(prevPage => prevPage - 1);
    }
  };

  return (
    <div>
      <button onClick={handlePrev} disabled={currentPage === 0}>
        이전
      </button>
      <span>{currentPage}</span> {/* 페이지를 1부터 시작하는 것처럼 표시 */}
      <button onClick={handleNext} disabled={currentPage === totalPages - 1}>
        다음
      </button>
    </div>
  );
}; 

export default PageButton;
