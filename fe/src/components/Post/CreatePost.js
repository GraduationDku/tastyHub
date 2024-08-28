import React, { useState } from "react";
import '../../css/Post/CreatePost.css';

function CreatePost() {
  const [formData, setFormData] = useState({
    title: '',
    text: ''
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
      const response = await fetch('http://localhost:8080/post/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('accessToken')
        },
        body: JSON.stringify({
          text: formData.text,
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
        <div className="title">
        제목 
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
        /></div>
        <br></br><br/>
        내용 
        <br/>
        <textarea
          type="text"
          name="text"
          value={formData.text}
          onChange={handleChange}
        />
        <br/><br/>
        <button type="submit">작성하기</button>
      </form>
      </div>
    </>
  );
}

export default CreatePost;
