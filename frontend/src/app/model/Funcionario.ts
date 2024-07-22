import { Usuario } from "./Usuario";

export class Funcionario {
    codigo?: number;
    nome!: string;
    usuario?: Usuario |  null;

}