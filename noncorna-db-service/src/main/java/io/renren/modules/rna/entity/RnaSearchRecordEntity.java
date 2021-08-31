package io.renren.modules.rna.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索记录表
 * 
 * @author hy
 * @email sunlightcs@gmail.com
 * @date 2020-10-13 09:28:26
 */
@Data
@TableName("rna_search_record")
@Accessors(chain = true)
public class RnaSearchRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long searchId;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * 创建时间
	 */
	private Date ct;
	/**
	 * gen 搜索条件
	 */
	private String gen;
	/**
	 * cancer 搜索条件
	 */
	private String cancer;
	/**
	 * immune_cell 搜索条件
	 */
	private String immuneCell;
	/**
	 * immune_pathway 搜索条件
	 */
	private String immunePathway;

}
