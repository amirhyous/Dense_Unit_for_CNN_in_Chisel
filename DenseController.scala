package solutions

import chisel3._
import chisel3.stage.ChiselStage

class DenseController extends Module {
	val io = IO(new Bundle{
		val start = Input(Bool())
		val co1 = Input(Bool())
		val co2 = Input(Bool())
		val cntEn = Output(Bool())
		val wrEn  = Output(Bool())
		val rdEn  = Output(Bool())
		val input_ld = Output(Bool())
		val weight_ld = Output(Bool())
        val out_ld = Output(Bool())
        val sel0 = Output(Bool())
		val sel1 = Output(Bool())
		val sel2 = Output(Bool())
	})
	
	val state = RegInit(0.U(2.W))
	io.cntEn := 0.U
	io.wrEn := 0.U
	io.rdEn:= 0.U
	io.input_ld := 0.U
	io.weight_ld:= 0.U
    io.out_ld := 0.U
	io.sel1 := 0.U
	io.sel2:= 0.U
    io.sel3:= 0.U
	
	when (state === 0.U) {
		when(start){
			state = 1.U
		} .otherwise { state = 0.U }
	}
	when(state === 1.U) {
		when(start) {
			state := 1.U
		} .otherwise { state := 2.U }
	}
    when(state === 2.U) {
        io.input_ld := 1.U
        io.rdEn := 1.U
		state = 3.U
	}
	when(state === 3.U) {
		io.rdEn := 1.U
		io.out_ld := 1.U
        io.weight_ld := 1.U
        io.cntEn := 1.U
		io.sel1 := 1.U
        io.sel2 := 1.U
		state = 4.U
	}
	when(state === 4.U) {
		io.rdEn := 1.U
        io.input_ld := 1.U
		state = 5.U
	}
	when(state === 5.U) {
		io.rdEn := 1.U
		io.out_ld := 1.U
        io.weight_ld := 1.U
        io.cntEn := 1.U
		io.sel1 := 1.U
        io.sel2 := 1.U
		when(co1) {
			state := 6.U
		} .otherwise { state := 4.U }
	}
	when(state === 6.U) {
        io.sel1 := 1.U
        io.sel0 := 1.U
		io.wrEn := 1.U
		when(co2) {
			state := 0.U
		} .otherwise { state := 2.U }
	}
	
}
