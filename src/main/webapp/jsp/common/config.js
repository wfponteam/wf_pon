var config={
	pon :'http://10.221.171.79:9011/pon/',
	user:['gxrwlc','gxrwlc1','gxrwlc2'],////
	getUser : function(status){
		var _user = ''
		switch (status) {
		case '施工':
			_user = this.user[0]
			break;
		case '施工(驳回)':
			_user = this.user[0]
			break;
		case '挂测(修改)':
			_user = this.user[1]
			break;
		default:
			_user = this.user[2]
			break;
		}
		
		return _user
	}
}