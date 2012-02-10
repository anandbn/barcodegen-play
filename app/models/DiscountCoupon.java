package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name="discount_coupon")
public class DiscountCoupon extends Model {
	@Id
	public String couponId;
	public String couponText;
	public String title;
	public String description;
	public int height;
	public int width;
	public String codeType;
	public String contentType;
	public DiscountCoupon(String couponId, String couponText, String title,
			String description, int height, int width, String codeType,String contentType) {
		super();
		this.couponId = couponId;
		this.couponText = couponText;
		this.title = title;
		this.description = description;
		this.height = height;
		this.width = width;
		this.codeType = codeType;
		this.contentType=contentType;
	}
	@Override
	public String toString() {
		return "DiscountCoupon [couponId=" + couponId + ", couponText="
				+ couponText + ", title=" + title + ", description="
				+ description + ", height=" + height + ", width=" + width
				+ ", codeType=" + codeType + ", contentType=" + contentType
				+ "]";
	}

}
