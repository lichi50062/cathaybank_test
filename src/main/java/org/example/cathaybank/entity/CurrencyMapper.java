package org.example.cathaybank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/**
 * @Author: Lichi
 * @Date:2024/02/03/上午 02:43
 * @Description:
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCY_MAPPER")
public class CurrencyMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CURRENCY_CODE")
    private String currencyCode;

    @Column(name = "CURRENCY_NAME")
    private String currencyName;

}