import React, { useState } from "react";
import '../../css/Post/Comment.css';

const Comment = ({ postId, refreshComments, comments }) => {
    const [text, setText] = useState('');
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editingText, setEditingText] = useState('');

    const createComment = async () => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/comment/create/${postId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': localStorage.getItem('accessToken')
                },
                body: JSON.stringify({ text })
            });
            if (response.ok) {
                setText('');
                refreshComments();
            } else {
                throw new Error('Failed to create comment');
            }
        } catch (error) {
            console.error('Error creating comment:', error);
        }
    };

    const editComment = async (commentId) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/comment/modify/${commentId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : localStorage.getItem('accessToken')
                },
                body:  {text: editingText }
            });
            if (response.ok) {
                
                setEditingCommentId(null);
                setEditingText('');
                refreshComments();
            } else {
                throw new Error('Failed to edit comment');
            }
        } catch (error) {
            console.error('Error editing comment:', error);
        }
    };

    const deleteComment = async (commentId) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/comment/delete/${commentId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : localStorage.getItem('accessToken')
                }
            });
            if (response.ok) {
                refreshComments();
            } else {
                throw new Error('Failed to delete comment');
            }
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    return (
        <div className="comment">
            <textarea
                value={text}
                onChange={(e) => setText(e.target.value)}
                placeholder="댓글을 작성하세요."
            ></textarea>
            <button className="btnsubmit" onClick={createComment}>Submit</button>

            {comments.map(comment => (
                <div key={comment.userId}>
                    {editingCommentId === comment.userId ? (
                        <>
                            <textarea
                                value={editingText}
                                onChange={(e) => setEditingText(e.target.value)}></textarea>
                            <button onClick={() => editComment(comment.userId)}>Save</button>
                            <button onClick={() => setEditingCommentId(null)}>Cancel</button>
                        </>
                    ) : (
                        <>
                            <p>{comment.text}</p>
                            <button onClick={() => setEditingCommentId(comment.userId)}>Edit</button>
                            <button onClick={() => deleteComment(comment.userId)}>Delete</button>
                        </>
                    )}
                </div>
            ))}
        </div>

    );
};

export default Comment;
