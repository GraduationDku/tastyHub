import React, { useState } from "react";

function CreatePost() {
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
      } else {
        throw new Error('Failed to create post');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Error creating post');
    }
  };

  return (
    <>
    <div className="createpost">
      
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
        /></div>
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
