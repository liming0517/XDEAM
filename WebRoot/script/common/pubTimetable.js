/**
 * 获取单双周数组
 * @xqzc 学期周次
 */
function parseWeekArray(xqzc){
	var weekArray = new Array();
	var oddArray = new Array();
	var evenArray = new Array();
	
	for(var i=1; i<parseInt(xqzc, 10)+1; i+=2){
		oddArray.push(i);
	}
	for(var i=2; i<parseInt(xqzc, 10)+1; i+=2){
		evenArray.push(i);
	}
	
	weekArray.push(oddArray);
	weekArray.push(evenArray);
	
	return weekArray;
}

/**解析周次显示方式
 * @param skzc 周次原数据
 * @param oddArray 单周周次
 * @param evenArray 双周周次
 * **/
function parseWeekShow(skzc, oddArray, evenArray){
	var result = '';
	var skzcArray = skzc.split('｜');
	var tempSkzcArray = new Array();
	var curSkzc = '';
	
	for(var i=0; i<skzcArray.length; i++){
		tempSkzcArray = skzcArray[i].split('&');
		
		if(i > 0){
			result += '｜';
		}
		
		for(var j=0; j<tempSkzcArray.length; j++){
			curSkzc = tempSkzcArray[j];
			
			if(j > 0){
				result += '&';
			}
			
			if(curSkzc.indexOf('-') > -1){
				result += curSkzc + '周';
			}else if(curSkzc == 'odd'){
				result += '单周';
			}else if(curSkzc == 'even'){
				result += '双周';
			}else if(curSkzc.indexOf('#')>-1 || (curSkzc.indexOf('#')<0&&curSkzc.length>0&&curSkzc.length<3)){
				//判断是否为单双周
				var curWeekArray = curSkzc.split('#');
				var oddFlag = true;
				var evenFlag = true;
				
				if(oddArray.length == curWeekArray.length){
					for(var i=0; i<oddArray.length; i++){
						if(oddArray[i] != curWeekArray[i]){
							oddFlag = false;
							break;
						}
					}
				}else{
					oddFlag = false;
				}
				
				if(evenArray.length == curWeekArray.length){
					for(var i=0; i<evenArray.length; i++){
						if(evenArray[i] != curWeekArray[i]){
							evenFlag = false;
							break;
						}
					}
				}else{
					evenFlag = false;
				}
				
				if(oddFlag == true)
					result += '单周';
				else if(evenFlag == true)
					result += '双周';
				else
					result += '第' + curSkzc.replace(/\#/g, ',') + '周';
			}else{
				result += curSkzc;
			}
		}
	}
	
	return result;
}

/**解析课程信息
	@param curCode 当前教师或教室编号
	@param courseInfo 原始课程信息
**/
function parseCourseInfo(curCode, courseInfo){
	var courseArray =  courseInfo.split('@');
	var resultArray = new Array();
	var skjhbhArray = new Array();//授课计划编号
	var codeArray = new Array();//授课教师/场地编号
	var nameArray = new Array();//授课教师/场地名称
	var courseNameArray = new Array();//课程名称
	var classNameArray = new Array();//班级名称
	var skzcArray = new Array();//授课周次
	var tempCodeArray = new Array();
	var tempNameArray = new Array();
	var tempSkzcArray = new Array();
	var tempCode = '';
	var tempName = '';
	
	for(var i=0; i<courseArray.length; i+=6){
		skjhbhArray = courseArray[i].split('｜');
		codeArray = courseArray[i+1].split('｜');
		nameArray = courseArray[i+2].split('｜');
		courseNameArray = courseArray[i+3].split('｜');
		classNameArray = courseArray[i+4].split('｜');
		skzcArray = courseArray[i+5].split('｜');
		
		for(var j=0; j<codeArray.length; j++){
			tempCodeArray = codeArray[j].split('&');
			tempNameArray = nameArray[j].split('&');
			tempSkzcArray = skzcArray[j].split('&');
			
			for(var k=0; k<tempCodeArray.length; k++){
				tempCode = tempCodeArray[k].split('+');
				tempName = tempNameArray[k].split('+');
				
				for(var a=0; a<tempCode.length; a++){
					//判断编号是否相同
					if(tempCode[a] == curCode){
						resultArray.push(skjhbhArray[j]);//授课计划编号
						resultArray.push(tempName[a]);//教师/场地名称
						resultArray.push(courseNameArray[j]);//课程名称
						resultArray.push(classNameArray[j]);//班级名称
						resultArray.push(parseSkzc(tempSkzcArray[k]));//授课周次
					}
				}
			}
		}
	}
	
	return resultArray;
}

/**解析授课周次，转换为单周格式[1,2,3]
	@param skzc 授课周次
**/
function parseSkzc(skzc){
	var xqzc = curXqzc;
	var resultArray = new Array();
	//判断授课周次是连续周次，还是特定周次（如单双周）
	//连续周次格式,如:1-18。
	//单双周，如:add单、even双。
	//特定周次格式,如：1#4#7#9
	if(skzc.indexOf('-') > -1){
		var tempStart = parseInt(skzc.split('-')[0], 10);
		var tempEnd = parseInt(skzc.split('-')[1], 10);
		
		for(var j=tempStart; j<tempEnd+1; j++){
			resultArray.push(j);
		}
	}else if('odd'==skzc || 'even'==skzc){//单双周
		var weekNum = parseInt(xqzc, 10);
		var tempNum = skzc=='odd'?1:2;
		for(var j=tempNum; j<weekNum+1; j+=2){
			resultArray.push(j);
		}
	}else{//特别指定的周次
		var tempArray = skzc.split('#');
		
		for(var j=0; j<tempArray.length; j++){
			resultArray.push(tempArray[j]);
		}
	}
	
	return resultArray;
}

/**解析授课周次，转换为单周格式1#2#3
	@param skzc 授课周次
**/
function formatterWeek(skzc){
	var xqzc = curXqzc;
	var result = '';
	//判断授课周次是连续周次，还是特定周次（如单双周）
	//连续周次格式,如:1-18。
	//单双周，如:add单、even双。
	//特定周次格式,如：1#4#7#9
	var skzcArray = skzc.split('｜');
	var tempSkzcArray = new Array();
	var tempSkzc = '';
	
	for(var i=0; i<skzcArray.length; i++){
		tempSkzcArray = skzcArray[i].split('&');
		
		for(var j=0; j<tempSkzcArray.length; j++){
			tempSkzc = tempSkzcArray[j];
			
			if(tempSkzc.indexOf('-') > -1){
				var tempStart = parseInt(tempSkzc.split('-')[0], 10);
				var tempEnd = parseInt(tempSkzc.split('-')[1], 10);
				
				for(var j=tempStart; j<tempEnd+1; j++){
					result += j+'#';
				}
			}else if('odd'==tempSkzc || 'even'==tempSkzc){//单双周
				var weekNum = parseInt(xqzc, 10);
				var tempNum = tempSkzc=='odd'?1:2;
				for(var j=tempNum; j<weekNum+1; j+=2){
					result += j+'#';
				}
			}else{//特别指定的周次
				var tempArray = tempSkzc.split('#');
				
				for(var j=0; j<tempArray.length; j++){
					result += tempArray[j]+'#';
				}
			}
		}
	}
	
	return result.substring(0, result.length-1);
}

/**解析课程拼接信息
	@param str 原始信息
**/
function parseInfoToArray(str){
	var temp_1 = str.split('｜');
	var temp_2 = '';
	
	var result = new Array();
	
	for(var i=0; i<temp_1.length; i++){
		temp_2 = temp_1[i].split('&');
		
		for(var j=0; j<temp_2.length; j++){
			result.push(temp_2[j]);
		}
	}
	
	return result;
}

/**判断授课周次是否冲突
* @param skzc_1 授课周次
* @param skzc_2 授课周次
* @return boolean
**/
function judgeSkzc(skzc_1, skzc_2){
	for(var i=0; i<skzc_1.length; i++){
		for(var j=0; j<skzc_2.length; j++){
			if(skzc_1[i] == skzc_2[j]){
				return true;
			}
		}
	}
	return false;
}

/**拼接排序
@param pjct  拼接冲突
@param tempskzcxq	授课周次详情
@param tempskjhmxbh	授课计划明细编号
@param tempstr 原始信息
**/
function pjpx(pjct, tempskzcxq, tempskjhmxbh, tempstr){
	var result = new Array();
	var result2 = new Array();
	var str = '';
	if(pjct){ //拼接冲突忽略时，先授课周次详情第一位大小排序，再以授课计划明细编号排序
		var temp_3 = tempskjhmxbh.split('｜');
		var temp_2 = tempstr.split('｜');
		var temp_1 = tempskzcxq.split('｜');
		for (var i = 0; i < temp_1.length; i++) { //遍历授课周次详情
			if(temp_1[i].indexOf('-') > -1){
				var num = parseInt(temp_1[i].split('-')[0], 10);
			}else if('odd'==temp_1[i] || 'even'==temp_1[i]){//单双周
				var num = temp_1[i]=='odd'?1:2;
			}else{//特别指定的周次
				var num = parseInt(temp_1[i].split('#')[0], 10);
			}
			result.push(num);
			result2.push(num);
		}
		result.sort(sortNumber); //数组排序
		for (var i = 0; i < result.length; i++) {
			var count = 0;
			for (var j = 0; j < result.length; j++) {
				if(result[i] == result[j]){
					count++;
				}
			}
			if(count > 1){
				var result3 = new Array();
				var result4 = new Array();
				var result5 = new Array();
				for (var j = 0; j < result2.length; j++) {
					if(result[i] == result2[j]){
						var num = temp_3[j].substring(temp_3[j].indexOf('_')+1);
						result3.push(num);
						result4.push(num);
						result5.push(j);
					}
				}
				result3.sort(sortNumber); //数组排序
				
				for (var j = 0; j < result3.length; j++) {
					var index = result4.indexOf(result3[j]);
					var index2 = result5[index]
					str += temp_2[index2] + '｜';
				}
				i +=count-1;
			}else{
				var index = result2.indexOf(result[i]);
				str += temp_2[index] + '｜';
			}
		}
		str = str.substring(0, str.length-1);
		
	}else{ //否则以授课周次详情第一位大小排序
		var temp_1 = tempskzcxq.split('｜');
		var temp_2 = tempstr.split('｜');
		for (var i = 0; i < temp_1.length; i++) {
			if(temp_1[i].indexOf('-') > -1){
				var num = parseInt(temp_1[i].split('-')[0], 10);
			}else if('odd'==temp_1[i] || 'even'==temp_1[i]){//单双周
				var num = temp_1[i]=='odd'?1:2;
			}else{//特别指定的周次
				var num = parseInt(temp_1[i].split('#')[0], 10);
			}
			result.push(num);
			result2.push(num);
		}
		result.sort(sortNumber); //数组排序
		for (var i = 0; i < result.length; i++) {
			var index = result2.indexOf(result[i]);
			str += temp_2[index] + '｜';
		}
		str = str.substring(0, str.length-1);
	}
	
	return str;
}

/**数据排序用
**/
function sortNumber(a,b){
	return a - b
}