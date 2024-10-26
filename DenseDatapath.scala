package solutions

import chisel3._
import chisel3.Data

class DenseDatapath(val input_number: Int, val weight_number: int) extends Module {
	val io = IO(new Bundle{
		val cntEn = Input(Bool())
		val wrEn  = Input(Bool())
		val rdEn  = Input(Bool())
		val input_ld = Input(Bool())
		val weight_ld = Input(Bool())
        val out_ld = Input(Bool())
        val sel0 = Input(Bool())
		val sel1 = Input(Bool())
		val sel2 = Input(Bool())
		val co1 = Output(Bool())
		val co2 = Output(Bool())
	})
	
	val counter1 = Module(new CostumCounter(input_number))
	counter1.io.cnt := cntEn
	counter1.io.co := co1

	val counter2 = Module(new CostumCounter(weight_number/input_number))
	counter2.io.cnt := co1
	counter2.io.co := co2

	val mux1 = Module(new Mux4())
	mux1.io.sel := {sel0,sel1}
	mux1.io.in0 := counter1.io.out
	mux1.io.in1 := counter2.io.out * input_number + counter1.io.out
    mux1.io.in2 := counter2.io.out + input_number + weight_number
    mux1.io.in3 := 0.U

	val memory = Mem(new Ram(32,256))
	memory.io.writeEnable := wrEn
	memory.io.readEnable := rdEn
	memory.io.address := mux1.io.out
	memory.io.dataIn := outReg.io.outp

    val inputReg = Module(new Register(32))
    inputReg.io.inp := memory.io.Result
    inputReg.io.en := input_ld

    val weightReg = Module(new Register(32))
    weightReg.io.inp := memory.io.Result
    weightReg.io.en := weight_ld

	val mux2 = Module(new Mux2())
	mux2.io.sel := sel2
	mux2.io.in1 := 0.U
	mux2.io.in0 := counter2.io.out * 25 + counter1.io.out

    val outReg = Module(new Register(32))
    outReg.io.inp := inputReg.io.outp * weightReg.io.outp + mux2.io.out
    outReg.io.en := out_ld

}