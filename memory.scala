import chisel3._
import chisel3.util.experimental.loadmemoryoryFromFile

class Mem(val length: Int, val DataLength) extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(log2Ceil(length).W))
    val wrData = Input(UInt(DataLength.W))
    val wrEn = Input(Bool())
    val rdEn = Input(Bool())
    val rdData = Output(UInt(DataLength.W))
  })

  val mem = Mem(length, UInt(DataLength.W))

  if (memoryFile.trim().nonEmpty) {
    loadMemoryFromFileInline(mem, "InputsAndWeights.txt")
  }

  when(io.wrEn) {
    mem.write(io.addr, io.wrData)
  }
  when(io.rdEn) {
    io.rdData := mem.read(io.addr)
  }
}