package solutions

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class MemTest(c: Mem) extends PeekPokeTester(c) {

  for (i <- 0 until 10) {

    val rdEn1 = rnd.nextInt(1 << c.w)
    val wrEn1 = rnd.nextInt(1 << c.w)

    poke(c.io.rdEn, rdEn1)
    poke(c.io.wrEn, wrEn1)

    val myVec = Vec.fill(10){ SInt(DataLength.W) }
    loadmemoryoryFromFile(myVec, "outputs.txt")
    
    step(1)
    expect(c.io.Result, (myVec(i))&((1 << c.w)-1))
  }
}
