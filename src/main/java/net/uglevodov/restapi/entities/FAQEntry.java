/*
 * Copyright (c) 2019. Timofei Ivanov, Uglevodov net, LLC
 */

package net.uglevodov.restapi.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faq")
@ApiModel( value = "Ответ", description = "Ответ на частый вопрос" )
public class FAQEntry extends BaseEntity {

    @ApiModelProperty( value = "Категория")
    @Column(name = "category")
    private Integer category;

    @ApiModelProperty( value = "Вопрос")
    @Column(name = "question")
    private String question;

    @ApiModelProperty( value = "Ответ")
    @Column(name = "answer")
    private String answer;


}
