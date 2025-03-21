package com.entity;

import com.annotation.ColumnInfo;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.*;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;


/**
 * 购票订单
 *
 * @author 
 * @email
 */
@TableName("checi_order")
public class CheciOrderEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public CheciOrderEntity() {

	}

	public CheciOrderEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 订单号
     */
    @ColumnInfo(comment="订单号",type="varchar(200)")
    @TableField(value = "checi_order_uuid_number")

    private String checiOrderUuidNumber;


    /**
     * 车次
     */
    @ColumnInfo(comment="车次",type="int(11)")
    @TableField(value = "checi_id")

    private Integer checiId;


    /**
     * 用户
     */
    @ColumnInfo(comment="用户",type="int(11)")
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 实付价格
     */
    @ColumnInfo(comment="实付价格",type="decimal(10,2)")
    @TableField(value = "checi_order_true_price")

    private Double checiOrderTruePrice;


    /**
     * 订单类型
     */
    @ColumnInfo(comment="订单类型",type="int(11)")
    @TableField(value = "checi_order_types")

    private Integer checiOrderTypes;


    /**
     * 车厢
     */
    @ColumnInfo(comment="车厢",type="int(11)")
    @TableField(value = "buy_section_number")

    private Integer buySectionNumber;


    /**
     * 购买的座位
     */
    @ColumnInfo(comment="购买的座位",type="varchar(200)")
    @TableField(value = "buy_zuowei_number")

    private String buyZuoweiNumber;


    /**
     * 订购日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	@DateTimeFormat
    @ColumnInfo(comment="订购日期",type="date")
    @TableField(value = "buy_zuowei_time")

    private Date buyZuoweiTime;


    /**
     * 订单创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="订单创建时间",type="timestamp")
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：订单号
	 */
    public String getCheciOrderUuidNumber() {
        return checiOrderUuidNumber;
    }
    /**
	 * 设置：订单号
	 */

    public void setCheciOrderUuidNumber(String checiOrderUuidNumber) {
        this.checiOrderUuidNumber = checiOrderUuidNumber;
    }
    /**
	 * 获取：车次
	 */
    public Integer getCheciId() {
        return checiId;
    }
    /**
	 * 设置：车次
	 */

    public void setCheciId(Integer checiId) {
        this.checiId = checiId;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }
    /**
	 * 设置：用户
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：实付价格
	 */
    public Double getCheciOrderTruePrice() {
        return checiOrderTruePrice;
    }
    /**
	 * 设置：实付价格
	 */

    public void setCheciOrderTruePrice(Double checiOrderTruePrice) {
        this.checiOrderTruePrice = checiOrderTruePrice;
    }
    /**
	 * 获取：订单类型
	 */
    public Integer getCheciOrderTypes() {
        return checiOrderTypes;
    }
    /**
	 * 设置：订单类型
	 */

    public void setCheciOrderTypes(Integer checiOrderTypes) {
        this.checiOrderTypes = checiOrderTypes;
    }
    /**
	 * 获取：车厢
	 */
    public Integer getBuySectionNumber() {
        return buySectionNumber;
    }
    /**
	 * 设置：车厢
	 */

    public void setBuySectionNumber(Integer buySectionNumber) {
        this.buySectionNumber = buySectionNumber;
    }
    /**
	 * 获取：购买的座位
	 */
    public String getBuyZuoweiNumber() {
        return buyZuoweiNumber;
    }
    /**
	 * 设置：购买的座位
	 */

    public void setBuyZuoweiNumber(String buyZuoweiNumber) {
        this.buyZuoweiNumber = buyZuoweiNumber;
    }
    /**
	 * 获取：订购日期
	 */
    public Date getBuyZuoweiTime() {
        return buyZuoweiTime;
    }
    /**
	 * 设置：订购日期
	 */

    public void setBuyZuoweiTime(Date buyZuoweiTime) {
        this.buyZuoweiTime = buyZuoweiTime;
    }
    /**
	 * 获取：订单创建时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }
    /**
	 * 设置：订单创建时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CheciOrder{" +
            ", id=" + id +
            ", checiOrderUuidNumber=" + checiOrderUuidNumber +
            ", checiId=" + checiId +
            ", yonghuId=" + yonghuId +
            ", checiOrderTruePrice=" + checiOrderTruePrice +
            ", checiOrderTypes=" + checiOrderTypes +
            ", buySectionNumber=" + buySectionNumber +
            ", buyZuoweiNumber=" + buyZuoweiNumber +
            ", buyZuoweiTime=" + DateUtil.convertString(buyZuoweiTime,"yyyy-MM-dd") +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
