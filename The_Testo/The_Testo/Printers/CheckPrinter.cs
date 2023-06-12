using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using Aspose.Words;
using Aspose.Words.Tables;
using The_Testo.Database;
using Xceed.Words.NET;


namespace The_Testo.Printers
{
    internal class CheckPrinter
    {
        public static void PrintCheque(Order order, string path)
        {
            try
            {
                var file = Properties.Resources.Template;
                File.WriteAllBytes("CopiedTemplate.docx", file);
            }
            catch
            {
                MessageBox.Show("Ошибка при открытии шаблона", "Ошибка", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            try
            {
                Stream stream = new FileStream(Path.GetFileName("CopiedTemplate.docx"), FileMode.Open);

                Document document = new Document(stream);
                DateTime orderDate = order.OrderDate;
                DocumentBuilder builder = new DocumentBuilder(document);
                document.Range.Replace("{0}", order.OrderDate.ToString());
                document.Range.Replace("{1}", order.OrderID.ToString());
                document.Range.Replace("{2}", order.User.UserSurname+" "+order.User.UserName);
                document.Range.Replace("{3}", order.Sum.ToString()+" руб.");
                Table table = (Table)document.GetChild(NodeType.Table, 0, true);


                // Add the row to the end of the table.
                foreach (Ordered_dishes item in order.Ordered_dishes)
                {
                    Row clonedRow = (Row)table.Rows[0].Clone(true);
                    clonedRow.Cells[0].Range.Replace("Блюдо", item.Dish.DishName);
                    clonedRow.Cells[1].Range.Replace("Кол-во", item.DishAmount.ToString());
                    clonedRow.Cells[2].Range.Replace("Цена", (item.Dish.DishPrice.ToString()+" руб."));
                    table.Rows.Add(clonedRow);
                }
                document.Save(path);
                MessageBox.Show("Успешно сохранено", "Уведомление", MessageBoxButton.OK, MessageBoxImage.Information);
            }
            catch (Exception) 
            {
                MessageBox.Show("Ошибка при создании файла", "Ошибка", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}
