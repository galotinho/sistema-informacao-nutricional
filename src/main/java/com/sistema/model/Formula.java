package com.sistema.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Formula {

	private List<Long> ingredientes;
	private List<Double> quantidades;
	private int porcao = 25;
}
