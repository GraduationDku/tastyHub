import React, { useState } from "react";
import '../../css/CreatePost.css';

function CreatePost({ setScreen }) {  // setScreen을 props로 받습니다.
  const [formData, setFormData] = useState({
    title: '',
    content: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/post/create`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
        body: JSON.stringify({
          content: formData.content,
          title: formData.title
        })
      });
      if (response.ok) {
        alert('Post created successfully!');
        setScreen('post');  // 작성 완료 후 'post' 화면으로 이동
      } else {
        throw new Error('Failed to create post');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error creating post');
    }
  };

  // 뒤로가기 버튼 클릭 시 'post' 화면으로 이동하는 함수
  const handleBack = () => {
    setScreen('post');
  };

  return (
    <>
    <br /><br/>
      <div className="createpost">
        {/* 뒤로가기 버튼 추가 */}
        <button className="back-button" onClick={handleBack}>&lt;</button>
        
        <form onSubmit={handleSubmit}>
          <br/>
          <br/>
          <div>
            <input
              type="text"
              name="title"
              className="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="제목을 입력하세요"
            />
          </div>
          <br></br><br/>
          
          <br/>
          <textarea
            type="text"
            name="content"
            className="postcontent"
            value={formData.content}
            onChange={handleChange}
            placeholder="내용을 입력하세요"
          />
          <br/><br/>
          <button type="submit">작성하기</button>
        </form>
      </div>
    </>
  );
}

export default CreatePost;
