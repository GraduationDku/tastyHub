// state들 보관하는 파일
import { configureStore } from '@reduxjs/toolkit'
import likeReducer from '../../src/redux/Like/likeState.js';
import loginReducer from '../../src/redux/User/loginState.js';

export default configureStore({
	reducer: {
		like : likeReducer,
		login : loginReducer,
	}
});