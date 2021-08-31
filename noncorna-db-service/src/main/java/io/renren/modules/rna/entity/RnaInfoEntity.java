package io.renren.modules.rna.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * rna信息
 * 
 * @author hy
 * @email sunlightcs@gmail.com
 * @date 2020-10-12 20:29:09
 */
@Data
@TableName("rna_info")
public class RnaInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long dataId;
	/**
	 * pmid
	 */
	@Excel(name = "PMID" ,orderNum = "1")
	private String pmid;

	/**
	 * tilte
	 */
	@Excel(name = "Title" ,orderNum = "2")
	private String tilte;
	/**
	 * journal
	 */
	@Excel(name = "Journal" ,orderNum = "3")
	private String journal;
	/**
	 * published
	 */
	@Excel(name = "Pub Time" ,orderNum = "4")
	private String pubTime;
	/**
	 * organism
	 */
	@Excel(name = "Organism" ,orderNum = "5")
	private String organism;
	/**
	 * tissue_origin
	 */
	@Excel(name = "Tissue Origin" ,orderNum = "6")
	private String tissueOrigin;
	/**
	 * cancer_type
	 */
	@Excel(name = "Cancer Type" ,orderNum = "7")
	private String cancerType;
	/**
	 * cancer_type_ad
	 */
	@Excel(name = "Cancer Type_ad" ,orderNum = "8")
	private String cancerTypeAd;

	/**
	 * database_id
	 */
	@Excel(name = "Database ID" ,orderNum = "9")
	private String databaseId;
	/**
	 * gene_id
	 */
	@Excel(name = "Gene ID" ,orderNum = "100")
	private String geneId;
	/**
	 * gene_symbol
	 */
	@Excel(name = "Gene Name" ,orderNum = "110")
	private String geneSymbol;

	/**
	 * gene_type
	 */
	@Excel(name = "Gene Type" ,orderNum = "120")
	private String geneType;
	/**
	 * gene_exp_pattern
	 */
	@Excel(name = "Gene Exp Pattern" ,orderNum = "130")
	private String geneExpPattern;
	/**
	 * suvival
	 */
	@Excel(name = "Survival" ,orderNum = "140")
	private String suvival;
	/**
	 * major_targets
	 */
	@Excel(name = "Major Targets" ,orderNum = "150")
	private String majorTargets;
	/**
	 * target_gene
	 */
	@Excel(name = "Target Gene" ,orderNum = "160")
	private String targetGene;
	/**
	 * downstream_effect
	 */
	@Excel(name = "Downstream Effect" ,orderNum = "170")
	private String downstreamEffect;
	/**
	 * downstream_effect_evidence
	 */
	@Excel(name = "Downstream Effect Evidence" ,orderNum = "180")
	private String downstreamEffectEvidence;
	/**
	 * downstream_effect_method
	 */
	@Excel(name = "Downstream Effect Method" ,orderNum = "190")
	private String downstreamEffectMethod;
	/**
	 * function
	 */
	@Excel(name = "Function" ,orderNum = "200")
	@TableField("`function`")
	private String function;
	/**
	 * funtion_evidence
	 */
	@Excel(name = "Funtion Evidence" ,orderNum = "210")
	private String funtionEvidence;
	/**
	 * function_method
	 */
	@Excel(name = "Function Method" ,orderNum = "220")
	private String functionMethod;
	/**
	 * immune_cell
	 */
	@Excel(name = "Immune Cell" ,orderNum = "230")
	private String immuneCell;
	/**
	 * immune_pathway
	 */
	@Excel(name = "Immune Pathway" ,orderNum = "240")
	private String immunePathway;
	/**
	 * immune_activity
	 */
	@Excel(name = "Immune Activity" ,orderNum = "250")
	private String immuneActivity;
	/**
	 * evidence_code
	 */
	@Excel(name = "Evidence Code" ,orderNum = "260")
	private String evidenceCode;
	/**
	 * status
	 */
	//@Excel(name = "status", replace = {"有效_0", "未审核_1"}, orderNum = "27")
	private Integer status;

}
