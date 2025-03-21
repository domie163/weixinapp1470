package com.entity.view;

import org.apache.tools.ant.util.DateUtils;
import com.annotation.ColumnInfo;
import com.entity.CheciCommentbackEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import com.utils.DateUtil;

/**
* 车次评价
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("checi_commentback")
public class CheciCommentbackView extends CheciCommentbackEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表

	//级联表 车次信息
		/**
		* 车次标题
		*/

		@ColumnInfo(comment="车次标题",type="varchar(200)")
		private String checiName;
		/**
		* 火车照片
		*/

		@ColumnInfo(comment="火车照片",type="varchar(200)")
		private String checiPhoto;
		/**
		* 火车类型
		*/
		@ColumnInfo(comment="火车类型",type="int(11)")
		private Integer checiTypes;
			/**
			* 火车类型的值
			*/
			@ColumnInfo(comment="火车类型的字典表值",type="varchar(200)")
			private String checiValue;
		/**
		* 现价
		*/
		@ColumnInfo(comment="现价",type="decimal(10,2)")
		private Double checiNewMoney;
		/**
		* 出发地
		*/

		@ColumnInfo(comment="出发地",type="varchar(200)")
		private String checiChufadi;
		/**
		* 目的地
		*/

		@ColumnInfo(comment="目的地",type="varchar(200)")
		private String checiMudidi;
		/**
		* 出发时间
		*/
		@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
		@DateTimeFormat
		@ColumnInfo(comment="出发时间",type="timestamp")
		private Date checiTime;
		/**
		* 车厢
		*/

		@ColumnInfo(comment="车厢",type="int(11)")
		private Integer sectionNumber;
		/**
		* 座位
		*/

		@ColumnInfo(comment="座位",type="int(11)")
		private Integer zuoweiNumber;
		/**
		* 是否上架
		*/
		@ColumnInfo(comment="是否上架",type="int(11)")
		private Integer shangxiaTypes;
			/**
			* 是否上架的值
			*/
			@ColumnInfo(comment="是否上架的字典表值",type="varchar(200)")
			private String shangxiaValue;
		/**
		* 逻辑删除
		*/

		@ColumnInfo(comment="逻辑删除",type="int(11)")
		private Integer checiDelete;
		/**
		* 经停站、到达时间详情
		*/

		@ColumnInfo(comment="经停站、到达时间详情",type="longtext")
		private String checiContent;
	//级联表 用户
		/**
		* 用户姓名
		*/

		@ColumnInfo(comment="用户姓名",type="varchar(200)")
		private String yonghuName;
		/**
		* 头像
		*/

		@ColumnInfo(comment="头像",type="varchar(255)")
		private String yonghuPhoto;
		/**
		* 用户手机号
		*/

		@ColumnInfo(comment="用户手机号",type="varchar(200)")
		private String yonghuPhone;
		/**
		* 用户身份证号
		*/

		@ColumnInfo(comment="用户身份证号",type="varchar(200)")
		private String yonghuIdNumber;
		/**
		* 余额
		*/
		@ColumnInfo(comment="余额",type="decimal(10,2)")
		private Double newMoney;
		/**
		* 假删
		*/

		@ColumnInfo(comment="假删",type="int(11)")
		private Integer yonghuDelete;



	public CheciCommentbackView() {

	}

	public CheciCommentbackView(CheciCommentbackEntity checiCommentbackEntity) {
		try {
			BeanUtils.copyProperties(this, checiCommentbackEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	//级联表的get和set 车次信息

		/**
		* 获取： 车次标题
		*/
		public String getCheciName() {
			return checiName;
		}
		/**
		* 设置： 车次标题
		*/
		public void setCheciName(String checiName) {
			this.checiName = checiName;
		}

		/**
		* 获取： 火车照片
		*/
		public String getCheciPhoto() {
			return checiPhoto;
		}
		/**
		* 设置： 火车照片
		*/
		public void setCheciPhoto(String checiPhoto) {
			this.checiPhoto = checiPhoto;
		}
		/**
		* 获取： 火车类型
		*/
		public Integer getCheciTypes() {
			return checiTypes;
		}
		/**
		* 设置： 火车类型
		*/
		public void setCheciTypes(Integer checiTypes) {
			this.checiTypes = checiTypes;
		}


			/**
			* 获取： 火车类型的值
			*/
			public String getCheciValue() {
				return checiValue;
			}
			/**
			* 设置： 火车类型的值
			*/
			public void setCheciValue(String checiValue) {
				this.checiValue = checiValue;
			}

		/**
		* 获取： 现价
		*/
		public Double getCheciNewMoney() {
			return checiNewMoney;
		}
		/**
		* 设置： 现价
		*/
		public void setCheciNewMoney(Double checiNewMoney) {
			this.checiNewMoney = checiNewMoney;
		}

		/**
		* 获取： 出发地
		*/
		public String getCheciChufadi() {
			return checiChufadi;
		}
		/**
		* 设置： 出发地
		*/
		public void setCheciChufadi(String checiChufadi) {
			this.checiChufadi = checiChufadi;
		}

		/**
		* 获取： 目的地
		*/
		public String getCheciMudidi() {
			return checiMudidi;
		}
		/**
		* 设置： 目的地
		*/
		public void setCheciMudidi(String checiMudidi) {
			this.checiMudidi = checiMudidi;
		}

		/**
		* 获取： 出发时间
		*/
		public Date getCheciTime() {
			return checiTime;
		}
		/**
		* 设置： 出发时间
		*/
		public void setCheciTime(Date checiTime) {
			this.checiTime = checiTime;
		}

		/**
		* 获取： 车厢
		*/
		public Integer getSectionNumber() {
			return sectionNumber;
		}
		/**
		* 设置： 车厢
		*/
		public void setSectionNumber(Integer sectionNumber) {
			this.sectionNumber = sectionNumber;
		}

		/**
		* 获取： 座位
		*/
		public Integer getZuoweiNumber() {
			return zuoweiNumber;
		}
		/**
		* 设置： 座位
		*/
		public void setZuoweiNumber(Integer zuoweiNumber) {
			this.zuoweiNumber = zuoweiNumber;
		}
		/**
		* 获取： 是否上架
		*/
		public Integer getShangxiaTypes() {
			return shangxiaTypes;
		}
		/**
		* 设置： 是否上架
		*/
		public void setShangxiaTypes(Integer shangxiaTypes) {
			this.shangxiaTypes = shangxiaTypes;
		}


			/**
			* 获取： 是否上架的值
			*/
			public String getShangxiaValue() {
				return shangxiaValue;
			}
			/**
			* 设置： 是否上架的值
			*/
			public void setShangxiaValue(String shangxiaValue) {
				this.shangxiaValue = shangxiaValue;
			}

		/**
		* 获取： 逻辑删除
		*/
		public Integer getCheciDelete() {
			return checiDelete;
		}
		/**
		* 设置： 逻辑删除
		*/
		public void setCheciDelete(Integer checiDelete) {
			this.checiDelete = checiDelete;
		}

		/**
		* 获取： 经停站、到达时间详情
		*/
		public String getCheciContent() {
			return checiContent;
		}
		/**
		* 设置： 经停站、到达时间详情
		*/
		public void setCheciContent(String checiContent) {
			this.checiContent = checiContent;
		}
	//级联表的get和set 用户

		/**
		* 获取： 用户姓名
		*/
		public String getYonghuName() {
			return yonghuName;
		}
		/**
		* 设置： 用户姓名
		*/
		public void setYonghuName(String yonghuName) {
			this.yonghuName = yonghuName;
		}

		/**
		* 获取： 头像
		*/
		public String getYonghuPhoto() {
			return yonghuPhoto;
		}
		/**
		* 设置： 头像
		*/
		public void setYonghuPhoto(String yonghuPhoto) {
			this.yonghuPhoto = yonghuPhoto;
		}

		/**
		* 获取： 用户手机号
		*/
		public String getYonghuPhone() {
			return yonghuPhone;
		}
		/**
		* 设置： 用户手机号
		*/
		public void setYonghuPhone(String yonghuPhone) {
			this.yonghuPhone = yonghuPhone;
		}

		/**
		* 获取： 用户身份证号
		*/
		public String getYonghuIdNumber() {
			return yonghuIdNumber;
		}
		/**
		* 设置： 用户身份证号
		*/
		public void setYonghuIdNumber(String yonghuIdNumber) {
			this.yonghuIdNumber = yonghuIdNumber;
		}

		/**
		* 获取： 余额
		*/
		public Double getNewMoney() {
			return newMoney;
		}
		/**
		* 设置： 余额
		*/
		public void setNewMoney(Double newMoney) {
			this.newMoney = newMoney;
		}

		/**
		* 获取： 假删
		*/
		public Integer getYonghuDelete() {
			return yonghuDelete;
		}
		/**
		* 设置： 假删
		*/
		public void setYonghuDelete(Integer yonghuDelete) {
			this.yonghuDelete = yonghuDelete;
		}


	@Override
	public String toString() {
		return "CheciCommentbackView{" +
			", checiName=" + checiName +
			", checiPhoto=" + checiPhoto +
			", checiNewMoney=" + checiNewMoney +
			", checiChufadi=" + checiChufadi +
			", checiMudidi=" + checiMudidi +
			", checiTime=" + DateUtil.convertString(checiTime,"yyyy-MM-dd") +
			", sectionNumber=" + sectionNumber +
			", zuoweiNumber=" + zuoweiNumber +
			", checiDelete=" + checiDelete +
			", checiContent=" + checiContent +
			", yonghuName=" + yonghuName +
			", yonghuPhoto=" + yonghuPhoto +
			", yonghuPhone=" + yonghuPhone +
			", yonghuIdNumber=" + yonghuIdNumber +
			", newMoney=" + newMoney +
			", yonghuDelete=" + yonghuDelete +
			"} " + super.toString();
	}
}
